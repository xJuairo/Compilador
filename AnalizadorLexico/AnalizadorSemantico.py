from analizadorsintactico import *
class SymbolTable:
    def __init__(self):
        self.symbols = {}

    def add_symbol(self, name, symbol_type, value, memory_location):
        if name in self.symbols:
            # Handle collisions by chaining
            existing_symbol = self.symbols[name]
            while existing_symbol.next:
                existing_symbol = existing_symbol.next
            new_symbol = Symbol(name, symbol_type, value, memory_location)
            existing_symbol.next = new_symbol
        else:
            self.symbols[name] = Symbol(name, symbol_type, value, memory_location)

    def lookup_symbol(self, name):
        if name in self.symbols:
            symbol = self.symbols[name]
            while symbol:
                yield symbol
                symbol = symbol.next

class Symbol:
    def __init__(self, name, symbol_type, value, memory_location):
        self.name = name
        self.symbol_type = symbol_type
        self.value = value
        self.memory_location = memory_location
        self.next = None

def annotate_ast_with_semantics(node, symbol_table):
    if node.value == "IntDeclaration":
        id_list_node = node.children[0]
        for id_node in id_list_node.children:
            name = id_node.value
            symbol_table.add_symbol(name, "int", None, None)

    if node.value == "FloatDeclaration":
        id_list_node = node.children[0]
        for id_node in id_list_node.children:
            name = id_node.value
            symbol_table.add_symbol(name, "float", None, None)

    if node.value == "Assignment":
        id_node = node.children[0]
        name = id_node.value
        symbol = symbol_table.lookup_symbol(name)
        if not list(symbol):
            print(f"Error: Variable '{name}' used without prior declaration at line {None}")
        else:
            if node.children[1].value == "=":
                value_node = node.children[2]
                validate_assignment_type(name, symbol, value_node, symbol_table)

    for child in node.children:
        annotate_ast_with_semantics(child, symbol_table)

def validate_assignment_type(name, symbol, value_node, symbol_table):
    expected_type = symbol.symbol_type
    actual_type = infer_type(value_node, symbol_table)
    if expected_type != actual_type:
        print(f"Error: Type mismatch for variable '{name}' at line {None}. "
              f"Expected {expected_type}, but got {actual_type}.")
    if actual_type == "error":
        print(f"Error: Invalid expression for variable '{name}' at line {value_node.line_number}.")

    if actual_type != "int" and actual_type != "float":
        print(f"Error: Invalid type for variable '{name}' at line {value_node.line_number}.")
        
def infer_type(node, symbol_table):
    if node.value == "num":
        return "int" if node.value.isdigit() else "float"
    if node.value == "id":
        name = node.children[0].value
        symbol = symbol_table.lookup_symbol(name)
        if not list(symbol):
            print(f"Error: Variable '{name}' used without prior declaration at line {None}")
            return "error"
        return symbol.symbol_type
    if node.value in ["+", "-", "*", "/", "(", ")"]:
        left_type = infer_type(node.children[0], symbol_table)
        right_type = infer_type(node.children[1], symbol_table)
        
        if left_type == "error" or right_type == "error":
            return "error"
        if left_type == "float" or right_type == "float":
            return "float"
        return "int"

def print_semantics_annotated_ast(node, level=0):
    indent = "  " * level
    annotation = f" [{node.type}: {node.value}]"
    print(f"{indent}- {node.value}{annotation}")
    for child in node.children:
        print_semantics_annotated_ast(child, level + 1)

def main():
    # Parse the code and get the abstract syntax tree
    with open('tokensformateados.txt', 'r') as file:
        lines = file.readlines()

    token_list = []
    for line in lines:
        line = line.strip("'\n[]")
        token_type, value = line.split("', '")
        token = Token(token_type, value)
        token_list.append(token)

    parser = Parser(token_list)
    ast = parser.parse()

    if parser.errors:
        print("Syntax Errors:")
        for error in parser.errors:
            print(error)
    else:
        print("Syntax is correct. Compilation successful.")

        # Create a symbol table and annotate the abstract syntax tree with semantics
        symbol_table = SymbolTable()
        annotate_ast_with_semantics(ast, symbol_table)

        # Print the annotated abstract syntax tree
        print("Annotated Abstract Syntax Tree:")
        print_semantics_annotated_ast(ast)

if __name__ == "__main__":
    main()
