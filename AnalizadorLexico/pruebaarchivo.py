# -*- coding: utf-8 -*-
import sys
import re

argumentos = '\n'.join(map(str, sys.argv))
# Lista de palabras reservadas
palabras_reservadas = {
    "then": "reserved word",
    "if": "reserved word",
    "else": "reserved word",
    "end": "reserved word",
    "do": "reserved word",
    "while": "reserved word",
    "repeat": "reserved word",
    "until": "reserved word",
    "cin": "reserved word",
    "cout": "reserved word",
    "real": "reserved word",
    "int": "reserved word",
    "boolean": "reserved word",
    "true": "reserved word",
    "false": "reserved word",
    "main": "reserved word"
}
palabras_reservadas_key = palabras_reservadas.keys()
# Lista de puntuación
puntuacion_grupos = {
    "(": "open parenthesis",
    ")": "close parenthesis",
    "{": "open brace",
    "}": "close brace",
    ";": "semicolon",
    ",": "comma"
}
puntuacion_grupos_key = puntuacion_grupos.keys()
# Lista de operadores aritméticos
aritmetica = {
    "+": "addition operator",
    "-": "subtraction operator",
    "*": "multiplication operator",
    "/": "division operator",
    "=": "assignment operator"
}
aritmetica_key = aritmetica.keys()
# Lista de operadores de comparación
comparaciones = {
    "==": "equal to",
    "!=": "not equal to",
    "<>": "not equal to",
    "<": "less than",
    ">": "greater than",
    "<=": "less than or equal to",
    ">=": "greater than or equal to"
}
comparaciones_key = comparaciones.keys()
# Lista de operadores lógicos
logicos = {
    "&&": "and operator",
    "||": "or operator",
    "!": "not operator"
}
logicos_key = logicos.keys()
# Lista de operadores dobles
dobles = {
    "++": "increment operator",
    "--": "decrement operator"
}
dobles_key = dobles.keys()
tokens = []
errors = []
row = 1
col = 1
i = 0
cadena = ''
while i < len(argumentos):
    col+=1
    if argumentos[i].isspace():
        if (argumentos[i] == "\n"):
            row +=1
            col = 1
        i += 1
        continue
    if argumentos[i:i+2] == "//":
        i = argumentos.index("\n", i)
        continue
    if argumentos[i:i+2] == "/*":
        i = argumentos.index("*/", i) + 2
        continue
    if argumentos[i].isalpha():
        j = i + 1
        while j < len(argumentos) and (argumentos[j].isalnum() or argumentos[j] == "_"):
            j += 1
        token = argumentos[i:j]
        if token in palabras_reservadas:
            tokens.append("[" + token + ", "  + palabras_reservadas[token] +"]")
        else:
            tokens.append("[" + token + ", identifier]")
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
            tokens.append("[" + argumentos[i:j] + ", float]")
        else:
            tokens.append("[" + argumentos[i:j] + ", integer]")
        i = j
        continue
    if argumentos[i] in puntuacion_grupos:
        tokens.append("[" + argumentos[i] + ", " +puntuacion_grupos[argumentos[i]]+"]")
        i += 1
        continue
    if argumentos[i:i+2] in comparaciones:
        tokens.append("[" + argumentos[i:i+2] +"," + comparaciones[argumentos[i:i+2]] +"]")
        i += 2
        continue
    elif argumentos[i] in comparaciones:
        tokens.append("[" + argumentos[i] + "," + comparaciones[argumentos[i]] +"]")
        i += 1
        continue
    if argumentos[i:i+2] in dobles:
        tokens.append("[" +argumentos[i:i+2] + "," + dobles[argumentos[i:i+2]] +"]")
        i += 2
        continue
    elif argumentos[i] in aritmetica:
        tokens.append("[" + argumentos[i] + ","+ aritmetica[argumentos[i]] + "]")
        i +=1
        continue
    else:
        errors.append("["+argumentos[i]+", error at row:" + str(row) +", column: " + str(col+1) + "]")
        i+=1
        continue
arch = open("tokens.txt","w")
arche = open("errors.txt","w")
print(len(tokens))
for item in tokens:
    print(item)
    arch.write(item + "\n")
arch.close()
for item in errors:
    print(item)
    arche.write(item + "\n")
arche.close()