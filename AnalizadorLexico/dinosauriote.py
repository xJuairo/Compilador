class CodeGenerator:
    def __init__(self):
        self.code = []
        self.temp_count = 0

    def get_temp(self):
        """ Obtiene un nuevo registro temporal """
        self.temp_count += 1
        return f"T{self.temp_count}"

    def generate_code(self, node):
        """ Genera código a partir del nodo del árbol sintáctico """
        if node.value in ["Program", "Statements", "Block"]:
            for child in node.children:
                self.generate_code(child)

        elif node.value == "Assignment":
            dest = node.children[0].value
            src_code = self.generate_expression_code(node.children[1])
            self.code.append(f"ST {src_code}, {dest}")

        elif node.value == "IfStatement":
            # Generar código para la condición
            condition_code = self.generate_expression_code(node.children[0])
            self.code.append(f"JNE {condition_code}, LABEL_IF_FALSE")
            # Generar código para el cuerpo del if
            self.generate_code(node.children[1])
            self.code.append("LABEL_IF_FALSE:")

        elif node.value == "WhileStatement":
            self.code.append("LABEL_WHILE_BEGIN:")
            condition_code = self.generate_expression_code(node.children[0])
            self.code.append(f"JNE {condition_code}, LABEL_WHILE_END")
            self.generate_code(node.children[1])
            self.code.append("JMP LABEL_WHILE_BEGIN")
            self.code.append("LABEL_WHILE_END:")

        # Implementar casos para otros tipos de nodos (DoWhileStatement, OutputStatement, etc.)
        elif node.value == "DoWhileStatement":
            self.code.append("LABEL_DO_WHILE_BEGIN:")
            for child in node.children[:-1]:  # Todos los hijos excepto la condición
                self.generate_code(child)
            condition_code = self.generate_expression_code(node.children[-1])
            self.code.append(f"JNE {condition_code}, LABEL_DO_WHILE_BEGIN")
        elif node.value == "OutputStatement":
            expr_code = self.generate_expression_code(node.children[0])
            self.code.append(f"OUT {expr_code}")
        elif node.value == "InputStatement":
            var_name = node.children[0].value  # Asume que el primer hijo es el nombre de la variable
            self.code.append(f"IN {var_name}")

    def generate_expression_code(self, node):
        """ Genera código para expresiones """
        if node.value in ["+", "-", "*", "/"]:
            left_code = self.generate_expression_code(node.children[0])
            right_code = self.generate_expression_code(node.children[1])
            result = self.get_temp()
            if node.value == "+":
                self.code.append(f"ADD {left_code}, {right_code}, {result}")
            elif node.value == "-":
                self.code.append(f"SUB {left_code}, {right_code}, {result}")
            elif node.value == "*":
                self.code.append(f"MUL {left_code}, {right_code}, {result}")
            elif node.value == "/":
                self.code.append(f"DIV {left_code}, {right_code}, {result}")
            return result

        elif self.is_int(node.value):
            temp = self.get_temp()
            self.code.append(f"LDC {temp}, {node.value}")
            return temp

        elif self.is_float(node.value):
            temp = self.get_temp()
            self.code.append(f"LDC {temp}, {node.value}")
            return temp

        elif node.identificator:
            return node.value  # Retorna el nombre de la variable

        # Dentro de generate_expression_code

        elif node.value in ["<", ">", "<=", ">=", "==", "!="]:
            left_code = self.generate_expression_code(node.children[0])
            right_code = self.generate_expression_code(node.children[1])
            result = self.get_temp()
            if node.value == "<":
                self.code.append(f"SUB {left_code}, {right_code}, {result}")
                self.code.append(f"JLT {result}, LABEL_TRUE")
            # Implementar casos similares para otros operadores relacionales

            return result  # Retornar el registro temporal que contiene el resultado


    def is_int(self, value):
        """ Verifica si un valor es un entero """
        try:
            int(value)
            return True
        except ValueError:
            return False

    def is_float(self, value):
        """ Verifica si un valor es un flotante """
        try:
            float(value)
            return True
        except ValueError:
            return False

# Uso del generador de código
code_generator = CodeGenerator()
code_generator.generate_code(root_node)  # root_node es el nodo raíz de tu árbol sintáctico
generated_code = code_generator.code

# Imprimir o guardar el código generado
for line in generated_code:
    print(line)

# Opcional: guardar en un archivo
with open("codigo_intermedio.txt", "w") as file:
    for line in generated_code:
        file.write(line + "\n")
