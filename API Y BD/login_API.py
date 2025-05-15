import os
import asyncpg
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

# Puedes también configurar estas variables en tu entorno
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://postgres:password@localhost:5432/applogin"
)

app = FastAPI(title="Auth API con FastAPI y PostgreSQL")

# Modelos de datos
class UserRegister(BaseModel):
    username: str
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

class Perfil(BaseModel):
    username: str
    talla: float
    peso: float
    edad: int
    genero: str
    meta: str
    diasSemana: int
    nivel: str
    observaciones: str

class ResponseModel(BaseModel):
    success: bool
    detail: str

# Crear pool y activar pgcrypto
@app.on_event("startup")
async def startup():
    app.state.db_pool = await asyncpg.create_pool(DATABASE_URL)
    async with app.state.db_pool.acquire() as conn:
        # Asegura que pgcrypto esté disponible
        await conn.execute("CREATE EXTENSION IF NOT EXISTS pgcrypto;")

@app.on_event("shutdown")
async def shutdown():
    await app.state.db_pool.close()

# Endpoint de registro
@app.post("/register", response_model=ResponseModel)
async def register(user: UserRegister):
    async with app.state.db_pool.acquire() as conn:
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
    async with app.state.db_pool.acquire() as conn:
        row = await conn.fetchrow(
            """
            SELECT id
              FROM usuarios
             WHERE username = $1
               AND password = crypt($2, password)
            """,
            user.username, user.password
        )
        if row is None:
            raise HTTPException(401, "Credenciales inválidas")
    return {"success": True, "detail": f"Bienvenido, usuario {row['id']}"}

@app.post("/perfil")
async def guardar_perfil(perfil: Perfil):
    async with app.state.db_pool.acquire() as conn:
        await conn.execute("""
            INSERT INTO perfiles (
                username, talla, peso, edad, genero, meta, dias_semana, nivel, observaciones
            ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
        """, perfil.username, perfil.talla, perfil.peso, perfil.edad,
             perfil.genero, perfil.meta, perfil.diasSemana, perfil.nivel, perfil.observaciones)
    return {"success": True, "detail": "Perfil guardado correctamente"}

# Para correr localmente:
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)