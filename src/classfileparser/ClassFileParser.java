package classfileparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

abstract class Parser {
	String[] constantPool;

	abstract byte[] readByte(int size);
}

public class ClassFileParser extends Parser {
	static final String FILE_NAME = "./bin/classfileparser/B.class";
	static final String HEX_CHAR = "0123456789ABCDEF";
	static final String MAGIC_STRING = "magic: ";
	static final String MINOR_VERSION = "minor_version: ";
	static final String MAJOR_VERSION = "major_version: ";
	static final String BRACKET = "{";
	static final String END_BRACKET = "}";
	static final String END_SQUARE_BRACKET = "]";
	static final String ACCESS_FLAGS = "access_flags: [ ";
	static final String THIS_CLASS = "this_class: { name_index: %1 }";
	static final String SUPER_CLASS = "super_class: { name_index: %1 }";
	static final String INDENT = "  ";
	static final String INTERFACES_COUNT = "interfaces_count: ";
	static final String INTERFACES = "interfaces: [";
	static final String CLASS_INDEX = "class_index: ";
	private static final String FIELDS_COUNT = "fields_count: ";
	private static final String FIELDS = "fields: [";
	private static final String NAME_INDEX = "name_index: ";
	private static final String DESCRIPTOR_INDEX = "descriptor_index: ";
	private static final String METHODS_COUNT = "methods_count: ";
	private static final String METHODS = "methods: [";

	String fileName;
	int index;
	byte[] rawClassByte;
	ConstantPoolParser constantPoolparser;
	AttributeParser attributeParser;

	public ClassFileParser(String fileName) {
		this.fileName = fileName;
		index = 0;
		constantPoolparser = new ConstantPoolParser(this);
		attributeParser = new AttributeParser(this);
	}

	void parseMagic() {
		byte[] bytes = readByte(4);
		System.out.println(MAGIC_STRING + Util.byteToHex(bytes));
	}

	void parseVersion() {
		byte[] bytes = readByte(2);
		System.out.println(MINOR_VERSION + Util.byteToHex(bytes));
		bytes = readByte(2);
		System.out.println(MAJOR_VERSION + Util.byteToHex(bytes));
	}

	void parseThisAndSuper() {
		byte[] bytes = readByte(2);
		System.out
				.println(THIS_CLASS.replace("%1", "" + Util.byteToInt(bytes)));
		bytes = readByte(2);
		System.out
				.println(SUPER_CLASS.replace("%1", "" + Util.byteToInt(bytes)));
	}

	void parseInterfaces() {
		int count = Util.byteToInt(readByte(2));
		System.out.println(INTERFACES_COUNT + count);
		System.out.print(INTERFACES);
		while (count-- > 0) {
			System.out.println(BRACKET);
			int interfaceIndex = Util.byteToInt(readByte(2));
			System.out.println(INDENT + CLASS_INDEX + interfaceIndex);
			System.out.print(END_BRACKET);
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	void parseFields() {
		int count = Util.byteToInt(readByte(2));
		System.out.println(FIELDS_COUNT + count);
		System.out.print(FIELDS);
		while (count-- > 0) {
			parseField();
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	void parseField() {
		System.out.println(BRACKET);
		int accessFlags = Util.byteToInt(readByte(2));
		System.out.print(INDENT + ACCESS_FLAGS);
		if ((accessFlags & Access.ACC_PUBLIC) > 0)
			System.out.print(Access.ACC_PUBLIC_STRING + " ");
		if ((accessFlags & Access.ACC_PRIVATE) > 0)
			System.out.print(Access.ACC_PRIVATE_STRING + " ");
		if ((accessFlags & Access.ACC_PROTECTED) > 0)
			System.out.print(Access.ACC_PROTECTED_STRING + " ");
		if ((accessFlags & Access.ACC_STATIC) > 0)
			System.out.print(Access.ACC_STATIC_STRING + " ");
		if ((accessFlags & Access.ACC_FINAL) > 0)
			System.out.print(Access.ACC_FINAL_STRING + " ");
		if ((accessFlags & Access.ACC_VOLATILE) > 0)
			System.out.print(Access.ACC_VOLATILE_STRING + " ");
		if ((accessFlags & Access.ACC_TRANSIENT) > 0)
			System.out.print(Access.ACC_TRANSIENT_STRING + " ");
		System.out.println(END_SQUARE_BRACKET);
		int nameIndex = Util.byteToInt(readByte(2));
		System.out.println(INDENT + NAME_INDEX + nameIndex);
		int descriptorIndex = Util.byteToInt(readByte(2));
		System.out.println(INDENT + DESCRIPTOR_INDEX + descriptorIndex);
		attributeParser.parseAttributes(1);
		System.out.print(END_BRACKET);
	}

	@Override
	public byte[] readByte(int size) {
		byte[] ret = new byte[size];
		int i = 0;
		while (index < rawClassByte.length && i < size) {
			ret[i++] = rawClassByte[index++];
		}
		return ret;
	}

	byte[] getBytesFromClass() throws IOException {
		File classFile = new File(fileName);
		FileInputStream classFileInput = new FileInputStream(classFile);
		int count = (int) classFile.length();
		byte[] rawClass = new byte[count];
		classFileInput.read(rawClass);
		return rawClass;
	}

	void parseMethods() {
		int count = Util.byteToInt(readByte(2));
		System.out.println(METHODS_COUNT + count);
		System.out.print(METHODS);
		while (count-- > 0) {
			System.out.println(BRACKET);
			System.out.print(INDENT + ACCESS_FLAGS);
			int accessFlags = Util.byteToInt(readByte(2));
			if ((accessFlags & Access.ACC_PUBLIC) > 0)
				System.out.print(Access.ACC_PUBLIC_STRING + " ");
			if ((accessFlags & Access.ACC_PRIVATE) > 0)
				System.out.print(Access.ACC_PRIVATE_STRING + " ");
			if ((accessFlags & Access.ACC_PROTECTED) > 0)
				System.out.print(Access.ACC_PROTECTED_STRING + " ");
			if ((accessFlags & Access.ACC_STATIC) > 0)
				System.out.print(Access.ACC_STATIC_STRING + " ");
			if ((accessFlags & Access.ACC_FINAL) > 0)
				System.out.print(Access.ACC_FINAL_STRING + " ");
			if ((accessFlags & Access.ACC_SYNCHRONIZED) > 0)
				System.out.print(Access.ACC_SYNCHRONIZED_STRING + " ");
			if ((accessFlags & Access.ACC_NATIVE) > 0)
				System.out.print(Access.ACC_NATIVE_STRING + " ");
			if ((accessFlags & Access.ACC_ABSTRACT) > 0)
				System.out.print(Access.ACC_ABSTRACT_STRING + " ");
			if ((accessFlags & Access.ACC_STRICT) > 0)
				System.out.print(Access.ACC_STRICT_STRING + " ");
			System.out.println(END_SQUARE_BRACKET);
			int nameIndex = Util.byteToInt(readByte(2));
			System.out.println(INDENT + NAME_INDEX + nameIndex);
			int descriptorIndex = Util.byteToInt(readByte(2));
			System.out.println(INDENT + DESCRIPTOR_INDEX + descriptorIndex);
			attributeParser.parseAttributes(1);
			System.out.print(END_BRACKET);
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	public void parse() throws IOException {
		rawClassByte = getBytesFromClass();
		printHex(rawClassByte);
		System.out.println();
		System.out.println("Parse start: =============================================");
		parseMagic();
		parseVersion();
		constantPoolparser.parseConstantPools(1);
		parseAccessFlags();
		parseThisAndSuper();
		parseInterfaces();
		parseFields();
		parseMethods();
		attributeParser.parseAttributes(0);
		System.out.println("End Parse: ===============================================");
	}

	void printHex(byte[] src) {
		System.out.println("Print Hex: ===============================================");
		for (int i = 0; i < src.length; i++) {
			int index = i % 0x10;
			if (index == 0) {
				System.out.print(String.format("%07x0h: ", i / 0x10));
			}
			String b = Util.byteToHex(rawClassByte[i]);
			System.out.print(b + " ");
			if (index == 0xf) {
				System.out.println();
			}
		}
		if(src.length % 16 != 0)
			System.out.println();
		System.out.println("End Print: ===============================================");
	}

	void parseAccessFlags() {
		byte[] bytes = readByte(2);
		int access = Util.byteToInt(bytes);
		System.out.print(ACCESS_FLAGS);
		if ((access & Access.ACC_PUBLIC) > 0)
			System.out.print(Access.ACC_PUBLIC_STRING + " ");
		if ((access & Access.ACC_FINAL) > 0)
			System.out.print(Access.ACC_FINAL_STRING + " ");
		if ((access & Access.ACC_SUPER) > 0)
			System.out.print(Access.ACC_SUPER_STRING + " ");
		if ((access & Access.ACC_INTERFACE) > 0)
			System.out.print(Access.ACC_INTERFACE_STRING + " ");
		if ((access & Access.ACC_ABSTRACT) > 0)
			System.out.print(Access.ACC_ABSTRACT_STRING + " ");
		System.out.println(END_BRACKET);
	}

	public static void main(String[] args) throws IOException {
		ClassFileParser cfp = new ClassFileParser(FILE_NAME);
		cfp.parse();
	}
}
