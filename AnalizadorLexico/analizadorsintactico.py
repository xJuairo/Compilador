# -*- coding: utf-8 -*-
class Token:
    def __init__(self, token_type, value, line_number):
        self.token_type = token_type
        self.value = value
        self.line_number = line_number


class Node:
    def __init__(self, value, children=None):
        self.value = value
        self.children = children or []

    def add_child(self, node):
        self.children.append(node)


class Parser:
    def __init__(self, tokens):
        self.tokens = tokens
        self.current_token = None
        self.token_index = -1
        self.errors = []
        self.advance()

    def advance(self):
        self.token_index += 1
        if self.token_index < len(self.tokens):
            self.current_token = self.tokens[self.token_index]
        else:
            self.current_token = None

    def match(self, token_type):
        if self.current_token and self.current_token.token_type == token_type:
            self.advance()
        else:
            expected_token = token_type if token_type else "end of input"
            found_token = self.current_token.token_type if self.current_token else "end of input"
            line_number = self.current_token.line_number if self.current_token else None
            self.errors.append(f"Error in line {line_number}: Expected '{expected_token}', found '{found_token}'")
            self.advance()

    def program(self):
        root = Node("Program")
        self.match("main")
        self.match("{")
        root.add_child(self.stmts())
        self.match("}")
        return root
    

    def stmts(self):
        root = Node("Statements")
        if self.current_token and self.current_token.token_type in ["int", "float", "id", "if", "while", "{", "cin", "cout"]:
            root.add_child(self.stmt())
        while self.current_token and self.current_token.token_type != "}":
            if self.current_token.token_type == "do":
                root.add_child(self.do_while_stmt())
            else:
                root.add_child(self.stmt())
        return root


  
    def do_while_stmt(self):
        root = Node("DoWhileStatement")
        self.match("do")
        root.add_child(self.stmt())  # Agregar la primera expresión dentro del do-while
        while self.current_token and self.current_token.token_type != "until":
            root.add_child(self.stmt())  # Agregar más expresiones dentro del do-while
        self.match("until")
        self.match("(")
        root.add_child(self.expr())
        self.match(")")
        self.match(";")
        return root


    def stmt(self):
        if self.current_token and self.current_token.token_type == "int":
            root = Node("IntDeclaration")
            self.match("int")
            root.add_child(self.idList())
            self.match(";")
        elif self.current_token and self.current_token.token_type == "do":
            root = self.do_while_stmt()
        elif self.current_token and self.current_token.token_type == "float":
            root = Node("FloatDeclaration")
            self.match("float")
            root.add_child(self.idList())
            self.match(";")
        elif self.current_token and self.current_token.token_type == "id":
                root = Node("Assignment")
                id_node = Node(self.current_token.value)
                self.match("id")
                if self.current_token and self.current_token.token_type in ["++", "--"]:
                    op_node = Node(self.current_token.value)
                    self.match(self.current_token.token_type)
                    root.add_child(op_node)
                elif self.current_token and self.current_token.token_type in ["<", ">", "<=", ">=", "==", "!="]:
                    op_node = Node(self.current_token.value)
                    self.match(self.current_token.token_type)
                    if self.current_token and self.current_token.token_type in ["id", "num"]:
                        operand_node = Node(self.current_token.value)
                        root.add_child(operand_node)
                        self.match(self.current_token.token_type)
                else:
                    self.match("=")
                    expr_node = self.expr()
                    root.add_child(expr_node)
                self.match(";")
                root.add_child(id_node)
        elif self.current_token and self.current_token.token_type == "if":
            root = Node("IfStatement")
            self.match("if")

            if self.current_token and self.current_token.token_type == "(":
                self.match("(")
                expr_node = self.expr()
                root.add_child(expr_node)
                self.match(")")

            stmt_node = self.stmt()

            if self.current_token and self.current_token.token_type == "{":
                self.match("{")
                root.add_child(stmt_node)
                self.match("}")
            else:
                root.add_child(stmt_node)

            if self.current_token and self.current_token.token_type == "else":
                self.match("else")
                else_stmt_node = self.stmt()
                root.add_child(else_stmt_node)

            self.match("end")
           
        elif self.current_token and self.current_token.token_type == "while":
            root = Node("WhileStatement")
            self.match("while")
            self.match("(")
            expr_node = self.expr()
            root.add_child(expr_node)
            self.match(")")
            stmt_node = self.stmt()
            root.add_child(stmt_node)
        elif self.current_token and self.current_token.token_type == "{":
            root = Node("Block")
            self.match("{")
            root.add_child(self.stmts())
            self.match("}")
        elif self.current_token and self.current_token.token_type == "cin":
            root = Node("InputStatement")
            self.match("cin")
            root.add_child(self.idList())
            self.match(";")
        elif self.current_token and self.current_token.token_type == "cout":
            root = Node("OutputStatement")
            self.match("cout")
            root.add_child(self.expr())
            self.match(";")
        else:
            root = Node("Error")
            error_token = self.current_token.value if self.current_token else None
            if error_token:
                self.errors.append(f"Sentencia inválida: {error_token}")
            self.advance()
        return root


    

    def idList(self):
        root = Node("IdList")
        id_node = Node(self.current_token.value)
        self.match("id")
        root.add_child(id_node)
        while self.current_token and self.current_token.token_type == ",":
            self.match(",")
            root.add_child(self.idList())
        return root

    def expr(self):
        root = self.term()
        while self.current_token and self.current_token.token_type in ["+", "-"]:
            op_node = Node(self.current_token.value)
            self.match(self.current_token.token_type)
            op_node.add_child(root)
            op_node.add_child(self.term())
            root = op_node
        return root
    
    def relational_expr(self):
        root = self.term()
        if self.current_token and self.current_token.token_type in ["<", ">", "<=", ">=", "==", "!="]:
            op_node = Node(self.current_token.value)
            self.match(self.current_token.token_type)
            root.add_child(op_node)
            if self.current_token and self.current_token.token_type in ["id", "num"]:
                operand_node = Node(self.current_token.value)
                root.add_child(operand_node)
                self.match(self.current_token.token_type)
        return root

    def term(self):
        root = self.factor()
        while self.current_token and self.current_token.token_type in ["*", "/", "%"]:
            op_node = Node(self.current_token.value)
            self.match(self.current_token.token_type)
            op_node.add_child(root)
            root = op_node
            root.add_child(self.factor())
        return root

    def factor(self):
        root = Node("Factor")
        if self.current_token and self.current_token.token_type == "(":
            self.match("(")
            root.add_child(self.expr())
            self.match(")")
        elif self.current_token and self.current_token.token_type in ["id", "num"]:
            value = self.current_token.value if self.current_token else None
            if value is not None:
                value_node = Node(value)
                root.add_child(value_node)
            self.match(self.current_token.token_type)
        else:
            root = Node("Error")
            error_token = self.current_token.value if self.current_token else None
            self.errors.append(f"Invalid factor: {error_token}")
            self.advance()
        return root

    def parse(self):
        ast = self.program()

        if self.errors:
            print("Syntax errors found. Compilation failed.")
        else:
            print("Syntax is correct. Compilation successful.")

        return ast

with open('tokensformateados.txt', 'r') as file:
    lines = file.readlines()

token_list = []

for line in lines:
    line = line.strip("'\n[]")
    if line:
        token_parts = line.split("', '")
        value = token_parts[0].strip()
        token_type = token_parts[1].strip()
        line_number = token_parts[2].strip()
        token_list.append(Token(value, token_type, line_number))

# Imprimir la lista de objetos Token
for tok in token_list:
    try:
        _ = tok.token_type  # Intentar acceder al atributo 'value'
    except AttributeError:
        print(f"Register {tok} cant generate 'NoneType' object has no attribute 'value'")


parser = Parser(token_list)
ast = parser.parse()

# Print errors
if parser.errors:
    # Abre un archivo en modo escritura
    with open("Sintaxiserrores.txt", "w") as archivo_errores:
        # Escribe el encabezado
        archivo_errores.write("Syntax Errors:\n")
        
        # Escribe cada error en una nueva línea
        for error in parser.errors:
            archivo_errores.write(error + "\n")

    # Imprime un mensaje indicando que se guardaron los errores en el archivo
else:
    print("No hay errores de sintaxis.")

# # Print AST
# def print_ast(node, level=0):
#     indent = " | " * level
#     print(indent + node.value)
#     for child in node.children:
#         print_ast(child, level + 1)

# print("Abstract Syntax Tree:")
# print_ast(ast)
def print_ast(node, level=0):
    indent = "  " * level
    print(f"{indent}- {node.value}")
    for child in node.children:
        print_ast(child, level + 1)

print("Abstract Syntax Tree:")
print_ast(ast)