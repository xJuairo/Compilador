import re

file = open("read.py")

operators = {'=' : 'Assignment op','+' : 'Addition op','-' : 'Subtraction op','/' : 'Division op','*' : 'Multiplication op','<' : 'Lessthan op','>' : 'Greaterthan op','%':'Mod op',
'<=':'Lessthanorequal op','>=':'Greaterthanorequal op','==':'Equal op','!=':'Different op','(':'Parentright op',')':'Parentleft op','{':'Leftbracket op','}':'Rightbracket op', 
'//':'Onelinecomment op','/*':'Commentleft','*/':'Commentright','++':'Increment op','--':'Decrement op' }
operators_key = operators.keys()

punctuation_symbol = { ':' : 'colon', ';' : 'semi-colon', '.' : 'dot' , ',' : 'comma' }
punctuation_symbol_key = punctuation_symbol.keys()

data_type = {'int':'tipo integer','real':'tipo flotante','boolean':'tipo boleano','char':'tipo char'}
data_type_key = data_type.keys()

identifier = {'[a-zA-Z]':'Letter','[0-9]':'Digit'}
identifier_key = identifier.keys()


a = file.read()
count = 0
program = a.split("\n")

for line in program:
	estado=0
	count = count + 1
	print("line#", count, "\n", line)

	tokens=line.split(' ')
	#x,y,z;
	tamanio=len(tokens)
	print("Los tokens son ", tokens)

	print('Line#', count, "Propiedades \n")
	for token in tokens:
		if(estado==2):
			tokens=token.split(',')
			print(tokens)
		print("Token: ",token)
		matchlet = re.search(list(identifier_key)[0],token)
		matchnum = re.search(list(identifier_key)[1],token)
		if token in operators_key:
			print("el operador es: ", operators[token])
		if token in data_type_key:
			print("el tipo de dato es: ", data_type[token])
			estado=2
		if token in punctuation_symbol_key:
			print("el simbolo de puntuacion es: ", punctuation_symbol[token])
		if matchlet:
			print("el identificador es: ", identifier[list(identifier_key)[0]])
		if matchnum:
			print("el identificador es: ", identifier[list(identifier_key)[1]])