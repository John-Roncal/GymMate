from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from openai import OpenAI
import asyncpg
from typing import Optional, List
import json
import re
from db_config import get_connection

app = FastAPI()

# Permitir solicitudes desde cualquier origen
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# =============================
# MODELOS Pydantic
# =============================
class ResponseModel(BaseModel):
    success: bool
    detail: str

class UserRegister(BaseModel):
    username: str
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

class Perfil(BaseModel):
    nombre: str
    edad: int
    peso: float
    talla: float
    genero: str
    meta: str
    dias_gym: int
    nivel: str
    observaciones: Optional[str] = None

class EjercicioDetalle(BaseModel):
    nombre: str
    descripcion: str
    repeticiones: str

class RutinaDetalle(BaseModel):
    nombre: str
    descripcion: str
    ejercicios: List[EjercicioDetalle]

class RespuestaIA(BaseModel):
    rutina: List[RutinaDetalle]
    nutricion: str

client = OpenAI(
    api_key="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",
    base_url="https://openrouter.ai/api/v1"
)

def consultar_chatbot(pregunta_usuario):
    mensajes = [
        {"role": "system", "content": "Eres un entrenador personal amigable y profesional de gimnasio. Responde a preguntas sobre ejercicios, rutinas y nutrición."}, #Si no tiene que ver con gym, que diga no es mi rubro.
        {"role": "user", "content": pregunta_usuario}
    ]

    respuesta = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=mensajes
    )

    return respuesta.choices[0].message.content
# =============================
# ENDPOINTS LOGIN
# =============================

# Endpoint de registro
@app.post("/register", response_model=ResponseModel)
async def register(user: UserRegister):
    conn = await get_connection()
    try:
        await conn.execute(
            """
            INSERT INTO usuarios (username, password)
            VALUES ($1, crypt($2, gen_salt('bf')))
            """,
            user.username, user.password
        )
    except asyncpg.exceptions.UniqueViolationError:
        raise HTTPException(400, "El nombre de usuario ya existe")
    return {"success": True, "detail": "Usuario registrado correctamente"}

# Endpoint de login
@app.post("/login", response_model=ResponseModel)
async def login(user: UserLogin):
    conn = await get_connection()
    try:
        row = await conn.fetchrow(
            """
            SELECT id
            FROM usuarios
            WHERE username = $1
            AND password = crypt($2, password)
            """,
            user.username, user.password
        )
    finally:
        await conn.close()

    if row is None:
        raise HTTPException(status_code=401, detail="Credenciales inválidas")

    return {"success": True, "detail": f"Bienvenido, usuario {row['id']}"}


# =============================
# ENDPOINTS
# =============================

@app.post("/perfil", response_model=RespuestaIA)
async def guardar_perfil(perfil: Perfil):
    conn = await get_connection()
    try:
        await conn.fetchrow(
            """
            INSERT INTO perfil (
                nombre, edad, peso, talla, genero, meta, dias_gym, nivel, observaciones
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
            """,
            perfil.nombre, perfil.edad, perfil.peso, perfil.talla, perfil.genero, perfil.meta, perfil.dias_gym, perfil.nivel, perfil.observaciones)
        return generar_plan(perfil)
    finally:
        await conn.close()

def generar_plan(usuario: Perfil):
    prompt = f"""
    Eres un entrenador personal virtual y nutricionista. Tu tarea es generar una rutina de ejercicios y recomendaciones nutricionales para una persona con los siguientes datos:

    Edad: {usuario.edad}
    Peso: {usuario.peso} kg
    Talla: {usuario.talla} cm
    Género: {usuario.genero}
    Meta: {usuario.meta}
    Días por semana que entrena: {usuario.dias_gym}
    Nivel de experiencia: {usuario.nivel}
    Observaciones: {usuario.observaciones if usuario.observaciones else 'Ninguna'}

    Devuelve exclusivamente un JSON válido con esta estructura:

    {{
    "rutina": [
        {{
        "nombre": "Nombre del día",
        "descripcion": "Descripción breve del enfoque del entrenamiento",
        "ejercicios": [
            {{
            "nombre": "Nombre del ejercicio",
            "descripcion": "Cómo se hace el ejercicio",
            "repeticiones": "Número de repeticiones por serie"
            }}
        }}
    ],
    "nutricion": "Plan nutricional."
    }}

    No agregues texto explicativo, etiquetas como ```json ni comentarios. Solo devuelve el objeto JSON.
    """

    try:
        contenido = consultar_chatbot(prompt)
        contenido = contenido.strip()
        contenido = re.search(r'\{.*\}', contenido, re.DOTALL).group(0)
        contenido = contenido.replace("'", '"')
        contenido = contenido.replace("{{", "{")
        return json.loads(contenido)

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error al procesar la respuesta de IA: {e}")

#uvicorn main:app --host 0.0.0.0 --port 8000