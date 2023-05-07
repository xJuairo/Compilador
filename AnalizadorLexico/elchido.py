import re

# Diccionarios con las palabras clave y los símbolos
palabras_clave = {'if', 'else', 'for', 'while', 'int', 'float', 'double', 'char'}
simbolos = {'=', ';', ',', '(', ')', '{', '}', '<', '>', '==', '!=', '<=', '>='}

# Diccionario con las palabras clave de las sentencias de control
sentencias_control = {'if', 'else', 'for', 'while'}

def lexico(cadena):
    tokens = []
    i = 0
    while i < len(cadena):
        # Saltar los espacios en blanco
        if cadena[i].isspace():
            i += 1
            continue
        
        # Identificar los números
        if cadena[i].isdigit():
            numero = ''
            while i < len(cadena) and (cadena[i].isdigit() or cadena[i] == '.'):
                numero += cadena[i]
                i += 1
            tokens.append(('NUMERO', numero))
            continue
        
        # Identificar los identificadores y palabras clave
        if cadena[i].isalpha():
            identificador = ''
            while i < len(cadena) and (cadena[i].isalnum() or cadena[i] == '_'):
                identificador += cadena[i]
                i += 1
            
            # Verificar si es una palabra clave o un identificador
            if identificador in palabras_clave:
                tokens.append((identificador.upper(), identificador))
            else:
                tokens.append(('IDENTIFICADOR', identificador))
            continue
        
        # Identificar los símbolos
        simbolo = cadena[i]
        if i < len(cadena) - 1 and (simbolo + cadena[i + 1]) in simbolos:
            simbolo += cadena[i + 1]
            i += 1
        if simbolo in simbolos:
            tokens.append(('SIMBOLO', simbolo))
            i += 1
            continue
        
        # Si no se reconoce el token, se genera un error
        raise ValueError('Token no reconocido: ' + cadena[i])
    
    return tokens

def main():
    # Leer el archivo de entrada
    with open('read.py') as archivo:
        lineas = archivo.readlines()

    # Analizar cada línea y generar los tokens
    for i, linea in enumerate(lineas):
        try:
            tokens = lexico(linea)
            print('Línea', i + 1, ':', tokens)
        except ValueError as error:
            print('Error en línea', i + 1, ':', error)

if __name__ == '__main__':
    main()