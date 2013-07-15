default: ./src/*java
	rm -f ./class/*~
	rm -f ./src/*~
	javac -d ./class ./src/*.java
clean:
	rm -f ./class/*class
	rm -f ./class/*ctxt
	rm -f ./class/*~
	rm -f ./src/*~
git:
	rm -f ./class/*class
	rm -f ./class/*ctxt
	rm -f ./class/*~
	rm -f ./src/*~
	git commit -a
