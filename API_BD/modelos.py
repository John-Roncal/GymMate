from pydantic import BaseModel
from typing import Optional, List

# =============================
# MODELOS Pydantic
# =============================
class ResponseModel(BaseModel):
    success: bool
    detail: str
    usuario_id: int

class UserRegister(BaseModel):
    username: str
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

class Perfil(BaseModel):
    usuario_id: int
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

class PreguntaRequest(BaseModel):
    pregunta: str

class RespuestaChat(BaseModel):
    respuesta: str