import os
import asyncpg
from dotenv import load_dotenv

load_dotenv() 

async def get_connection():
    return await asyncpg.connect(
        host=os.getenv("DB_HOST", "localhost"),
        database=os.getenv("DB_NAME", "gymmate_bd"),
        user=os.getenv("DB_USER", "postgres"),
        password=os.getenv("DB_PASS", "password"),
        port=os.getenv("DB_PORT", "5432")
    )
