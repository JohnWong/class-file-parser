package classfileparser;

class SubParser {
	int indent;
	static final String INDENT = "  ";
	Parser parser;

	SubParser(Parser parser) {
		this.parser = parser;
	}

	void println(Object o) {
		print(o);
		System.out.println();
	}
	
	void print(Object o){
		for (int i = 0; i < indent; i++)
			System.out.print(INDENT);
		System.out.print(o);
	}
}
