archs = open("tokens.txt","r")
cadena = archs.read()

# Paso 1: Separar la cadena por saltos de línea
lineas = cadena.split('\n')

# Paso 2: Crear una lista de sublistas con dos columnas
array_dos_columnas = [linea.strip('[]').split(', ') for linea in lineas if linea.strip()]

# Imprimir el resultado
for fila in array_dos_columnas:
    print(fila[1] + ", " + fila[0])