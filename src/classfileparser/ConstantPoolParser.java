package classfileparser;

import java.nio.charset.Charset;

class ConstantPoolParser extends SubParser {
	static final String CONSTANT_UTF8_INFO = "CONSTANT_Utf8_info[%1]: {";
	static final int CONSTANT_UTF8 = 1;
	static final String CONSTANT_INTEGER_INFO = "CONSTANT_Integer_info[%1]: {";
	static final int CONSTANT_INTEGER = 3;
	static final String CONSTANT_FLOAT_INFO = "CONSTANT_Float_info[%1]: {";
	static final int CONSTANT_FLOAT = 4;
	static final String CONSTANT_LONG_INFO = "CONSTANT_Long_info[%1]: {";
	static final int CONSTANT_LONG = 5;
	static final String CONSTANT_DOUBLE_INFO = "CONSTANT_Double_info[%1]: {";
	static final int CONSTANT_DOUBLE = 6;
	static final String CONSTANT_CLASS_INFO = "CONSTANT_Class_info[%1]: {";
	static final int CONSTANT_CLASS = 7;
	static final String CONSTANT_STRING_INFO = "CONSTANT_String_info[%1]: {";
	static final int CONSTANT_STRING = 8;
	static final String CONSTANT_FIELDREF_INFO = "CONSTANT_Fieldref_info[%1]: {";
	static final int CONSTANT_FIELDREF = 9;
	static final String CONSTANT_METHODREF_INFO = "CONSTANT_Methodref_info[%1]: {";
	static final int CONSTANT_METHODREF = 10;
	static final String CONSTANT_INTERFACEMETHODREF_INFO = "CONSTANT_InterfaceMethodref_info[%1]: {";
	static final int CONSTANT_INTERFACEMETHODREF = 11;
	static final String CONSTANT_NAMEANDTYPE_INFO = "CONSTANT_NameAndType_info[%1]: {";
	static final int CONSTANT_NAMEANDTYPE = 12;
	static final String CONSTANT_POOL_SIZE = "constant_pool_count: ";
	static final String CONSTANT_POOL = "constant_pool: [";
	static final String LENGTH = "length: ";
	static final String BYTES = "bytes: ";
	static final String TAG = "tag: ";
	static final String NAME_INDEX = "name_index: ";
	static final String STRING_INDEX = "string_index: ";
	static final String CLASS_INDEX = "class_index: ";
	static final String NAME_AND_TYPE_INDEX = "name_and_type_index: ";
	static final String DESCRIPTOR_INDEX = "descriptor_index: ";
	static final String END_BRACKET = "}";
	static final String END_SQUARE_BRACKET = "]";

	public ConstantPoolParser(Parser parser) {
		super(parser);
	}

	void parseConstantPools(int indent) {
		this.indent = indent;
		byte[] constantPoolByte = parser.readByte(2);
		int constantPoolCount = Util.byteToInt(constantPoolByte);
		parser.constantPool = new String[constantPoolCount + 1];
		System.out.println(CONSTANT_POOL_SIZE
				+ Util.byteToHex(constantPoolByte));
		System.out.println(CONSTANT_POOL);
		for (int i = 1; i < constantPoolCount; i++) {
			i = parseConstantPool(i);
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	int parseConstantPool(int count) {
		byte[] tagByte = parser.readByte(1);
		int tag = Util.byteToInt(tagByte);
		switch (tag) {
		case CONSTANT_UTF8:
			parseUtf8Info(tagByte, count);
			break;
		case CONSTANT_INTEGER:
			parseCommonTypeInfo(tagByte, count, 4, CONSTANT_INTEGER_INFO,
					CONSTANT_INTEGER);
			break;
		case CONSTANT_FLOAT:
			parseCommonTypeInfo(tagByte, count, 4, CONSTANT_FLOAT_INFO,
					CONSTANT_FLOAT);
			break;
		case CONSTANT_LONG:
			parseCommonTypeInfo(tagByte, count, 8, CONSTANT_LONG_INFO,
					CONSTANT_LONG);
			count++;
			break;
		case CONSTANT_DOUBLE:
			parseCommonTypeInfo(tagByte, count, 8, CONSTANT_DOUBLE_INFO,
					CONSTANT_DOUBLE);
			count++;
			break;
		case CONSTANT_CLASS:
			parseClassInfo(tagByte, count);
			break;
		case CONSTANT_STRING:
			parseStringInfo(tagByte, count);
			break;
		case CONSTANT_FIELDREF:
			parseCommonMethodInfo(tagByte, count, CONSTANT_FIELDREF_INFO);
			break;
		case CONSTANT_METHODREF:
			parseCommonMethodInfo(tagByte, count, CONSTANT_METHODREF_INFO);
			break;
		case CONSTANT_INTERFACEMETHODREF:
			parseCommonMethodInfo(tagByte, count,
					CONSTANT_INTERFACEMETHODREF_INFO);
			break;
		case CONSTANT_NAMEANDTYPE:
			parseNameAndTypeInfo(tagByte, count);
			break;
		default:
			System.out.println("can not " + tag);
		}
		return count;
	}

	void parseUtf8Info(byte[] tag, int count) {
		byte[] lengthByte = parser.readByte(2);
		int length = Util.byteToInt(lengthByte);
		byte[] content = parser.readByte(length);
		println(CONSTANT_UTF8_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		println(INDENT + LENGTH + length);
		String info = new String(content, Charset.forName("utf-8"));
		println(INDENT + BYTES + info);
		parser.constantPool[count] = info;
		println(END_BRACKET);
	}

	void parseCommonTypeInfo(byte[] tag, int count, int byteCount, String msg,
			int type) {
		byte[] bytes = parser.readByte(byteCount);
		println(msg.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		print(INDENT + BYTES);
		if (type == CONSTANT_INTEGER)
			println(Util.byteToInt(bytes));
		else if (type == CONSTANT_FLOAT)
			System.out.println(Util.byteToFloat(bytes));
		else if (type == CONSTANT_LONG)
			System.out.println(Util.byteToLong(bytes));
		else if (type == CONSTANT_DOUBLE)
			System.out.println(Util.byteToDouble(bytes));
		else
			System.out.println(Util.byteToHex(bytes));
		println(END_BRACKET);
	}

	void parseFloatInfo(byte[] tag, int count) {
		byte[] bytes = parser.readByte(4);
		println(CONSTANT_FLOAT_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		println(BYTES + Util.byteToHex(bytes));
		println(END_BRACKET);
	}

	void parseLongInfo(byte[] tag, int count) {
		byte[] bytes = parser.readByte(8);
		println(CONSTANT_LONG_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		println(BYTES + Util.byteToHex(bytes));
		println(END_BRACKET);
	}

	void parseClassInfo(byte[] tag, int count) {
		byte[] bytes = parser.readByte(2);
		println(CONSTANT_CLASS_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		println(INDENT + NAME_INDEX + Util.byteToInt(bytes));
		println(END_BRACKET);
	}

	void parseStringInfo(byte[] tag, int count) {
		byte[] bytes = parser.readByte(2);
		println(CONSTANT_STRING_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		println(INDENT + STRING_INDEX + Util.byteToInt(bytes));
		println(END_BRACKET);
	}

	void parseCommonMethodInfo(byte[] tag, int count, String msg) {
		println(msg.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		byte[] classIndex = parser.readByte(2);
		println(INDENT + CLASS_INDEX + Util.byteToInt(classIndex));
		byte[] nameAndTypeIndex = parser.readByte(2);
		println(INDENT + NAME_AND_TYPE_INDEX + Util.byteToInt(nameAndTypeIndex));
		println(END_BRACKET);

	}

	void parseNameAndTypeInfo(byte[] tag, int count) {
		println(CONSTANT_NAMEANDTYPE_INFO.replace("%1", "" + count));
		println(INDENT + TAG + Util.byteToHex(tag));
		byte[] nameIndex = parser.readByte(2);
		println(INDENT + NAME_INDEX + Util.byteToInt(nameIndex));
		byte[] descriptorIndex = parser.readByte(2);
		println(INDENT + DESCRIPTOR_INDEX + Util.byteToInt(descriptorIndex));
		println(END_BRACKET);
	}

}
