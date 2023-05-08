# -*- coding: utf-8 -*-
import sys
import re

argumentos = ""
for i in range(0,len(sys.argv)):
    argumentos += str(sys.argv[i])
    argumentos+='\n'

# Diccionarios de tokens
palabras_reservadas = {"if": "palabra reservada", "else": "palabra reservada", "end": "palabra reservada", "do": "palabra reservada", "while": "palabra reservada", "repeat": "palabra reservada", "until": "palabra reservada", "cin": "palabra reservada", "cout": "palabra reservada", "real": "palabra reservada", "int": "palabra reservada", "boolean": "palabra reservada", "true": "palabra reservada", "false": "palabra reservada"}
palabras_reservadas_key = palabras_reservadas.keys()
puntuacion_grupos = {"(": "parentesis abierto", ")": "parentesis cerrado", "{": "llave abierto", "}": "llave cerrado", ";": "punto y coma", ",": "coma"}
puntuacion_grupos_key = puntuacion_grupos.keys()
aritmetica = {"+": "operador suma", "-": "operador resta", "*": "operador multiplicacion", "/": "operador division", "=": "operador asignacion"}
aritmetica_key = aritmetica.keys()
comparaciones = {"==": "operador iguales", "!=": "operador diferente", "<>":"operador diferente","<": "operador menor que", ">": "operador mayor que", "<=": "operador menor o igual que", ">=": "operador mayor o igual que"}
comparaciones_key = comparaciones.keys()
logicos = {"&&": "operador and", "||": "operador or", "!": "operador not"}
logicos_key = logicos.keys()
dobles = {"++": "operador incremento", "--": "operador decremento"}
dobles_key = dobles.keys()
# Tokenizar argumentos
tokens = []
linea = 1
col = 1
i = 0
while i < len(argumentos):
    # Ignorar espacios en blanco
    col+=1
    if argumentos[i].isspace():
        if (argumentos[i] == "\n"):
            linea +=1
            col = 1
        i += 1
        continue
    # Identificar comentarios de una línea
    if argumentos[i:i+2] == "//":
        i = argumentos.index("\n", i)
        continue
    # Identificar comentarios multilinea
    if argumentos[i:i+2] == "/*":
        i = argumentos.index("*/", i) + 2
        continue
    # Identificar palabras reservadas, identificadores y números
    if argumentos[i].isalpha():
        j = i + 1
        while j < len(argumentos) and (argumentos[j].isalnum() or argumentos[j] == "_"):
            j += 1
        token = argumentos[i:j]
        if token in palabras_reservadas:
            tokens.append("[" + token + ", "  + palabras_reservadas[token] +"]")
        else:
            tokens.append("[" + token + ", ídentificador]")
        i = j
        continue
    elif argumentos[i].isdigit():
        j = i + 1
        while j < len(argumentos) and argumentos[j].isdigit():
            j += 1
        if j < len(argumentos) and argumentos[j] == ".":
            j += 1
            while j < len(argumentos) and argumentos[j].isdigit():
                j += 1
            tokens.append("[" + argumentos[i:j] + ", flotante]")
        else:
            tokens.append("[" + argumentos[i:j] + ", entero]")
        i = j
        continue
    # Identificar símbolos especiales
    if argumentos[i] in puntuacion_grupos:
        tokens.append("[" + argumentos[i] + ", simbolo especial]")
        i += 1
        continue
    # Identificar operadores aritméticos y relacionales
    if argumentos[i:i+2] in comparaciones:
        tokens.append("[" + comparaciones[argumentos[i:i+2]] + ", operador relacional]")
        i += 2
        continue
    elif argumentos[i] in comparaciones:
        tokens.append("[" + comparaciones[argumentos[i]] + ", operador relacional]")
        i += 1
        continue
    if argumentos[i:i+2] in dobles:
        tokens.append("[" +argumentos[i:i+2] + ", operador aritmetico]")
        i += 2
        continue
    elif argumentos[i] in aritmetica:
        tokens.append("[" + argumentos[i] + ", operador aritmetico]")
        i +=1
        continue
    else: 
        tokens.append("error (linea:" + str(linea) +", columna: " + str(col+1) + ")")
        i+=1
        continue
for item in tokens:
    print(item)