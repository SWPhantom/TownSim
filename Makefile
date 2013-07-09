default: *java
	rm -f *~
	javac *java
clean:
	rm -f *class
	rm -f *ctxt
	rm -f *~
git:
	rm -f *class
	rm -f *ctxt
	rm -f *~
	git commit -a
