from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from openai import OpenAI
import asyncpg
import json
import re
from db_config import get_connection
import modelos as m

app = FastAPI()

# Permitir solicitudes desde cualquier origen
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

client = OpenAI(
    api_key="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",
    base_url="https://openrouter.ai/api/v1"
)

def consultar_chatbot(pregunta_usuario):
    mensajes = [
        {"role": "system", "content": "Eres un entrenador personal amigable y profesional de gimnasio. Responde a preguntas sobre ejercicios, rutinas y nutrición. Responde siempre de forma concisa y en un tono conversacional. Ten en cuenta que tu respuesta forma parte de una conversación de un chatbot, sin utilizar asteriscos ni ningún otro símbolo para énfasis o formato."},
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
@app.post("/register", response_model=m.ResponseModel)
async def register(user: m.UserRegister):
    conn = await get_connection()
    try:
        row = await conn.fetchrow(
            """
            INSERT INTO usuarios (username, password)
            VALUES ($1, crypt($2, gen_salt('bf')))
            RETURNING id
            """,
            user.username, user.password
        )
        usuario_id = row["id"]
        return {
            "success": True,
            "detail": "Usuario registrado correctamente.",
            "usuario_id": usuario_id
        }
    except asyncpg.exceptions.UniqueViolationError:
        raise HTTPException(400, "El nombre de usuario ya existe")
    finally:
        await conn.close()

# Endpoint de login
@app.post("/login", response_model=m.ResponseModel)
async def login(user: m.UserLogin):
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
    
    usuario_id = row["id"]
    return {
        "success": True, 
        "detail": "Bienvenido", 
        "usuario_id": usuario_id
    }


# =============================
# ENDPOINTS
# =============================

@app.post("/chatbot", response_model=m.RespuestaChat)
async def responder_chatbot(pregunta: m.PreguntaRequest):
    respuesta_ia = consultar_chatbot(pregunta.pregunta)
    return {"respuesta": respuesta_ia}

@app.post("/perfil", response_model=m.RespuestaIA)
async def guardar_perfil(perfil: m.Perfil):
    conn = await get_connection()
    try:
        await conn.fetchrow(
            """
            INSERT INTO perfil ( 
            usuario_id, nombre, edad, peso, talla, genero, meta, dias_gym, nivel, observaciones
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
            """,
            perfil.usuario_id, perfil.nombre, perfil.edad, perfil.peso, perfil.talla, perfil.genero, perfil.meta, perfil.dias_gym, perfil.nivel, perfil.observaciones)
        
        plan = generar_plan(perfil)

        await conn.execute("""
            INSERT INTO plan_entrenamiento (usuario_id, rutina, nutricion)
            VALUES ($1, $2, $3)
        """, perfil.usuario_id, json.dumps(plan["rutina"]), plan["nutricion"])

        return plan
    finally:
        await conn.close()

@app.get("/plan/{usuario_id}", response_model=m.RespuestaIA)
async def obtener_plan_guardado(usuario_id: int):
    conn = await get_connection()
    try:
        row = await conn.fetchrow(
            "SELECT rutina, nutricion FROM plan_entrenamiento WHERE usuario_id = $1",
            usuario_id
        )      
        if row is None:
            raise HTTPException(status_code=404, detail="Plan no encontrado")

        rutina = json.loads(row['rutina'])
        nutricion = row['nutricion']
        
        return {"rutina": rutina, "nutricion": nutricion}
    finally:
        await conn.close()


def generar_plan(usuario: m.Perfil):
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