package classfileparser;

import java.util.Arrays;

import com.sun.org.apache.bcel.internal.util.ClassPath.ClassFile;

public class AttributeParser extends SubParser {

	static final String ATTRIBUTES_COUNT = "attributes_count: ";
	static final String CODE_ATTR = "Code";
	private static final String CONSTANT_VALUE_ATTR = "ConstantValue";
	private static final String DEPRECATED_ATTR = "Deprecated";
	private static final String EXCEPTIONS_ATTR = "Exceptions";
	private static final String INNER_CLASSES_ATTR = "InnerClasses";
	private static final String LINE_NUMBER_TABLE_ATTR = "LineNumberTable";
	private static final String LOCAL_VARIABLE_TABLE_ATTR = "LocalVariableTable";
	private static final String SOURCE_FILE_ATTR = "SourceFile";
	private static final String SYNTHETIC_ATTR = "Synthetic";
	private static final String MAX_STACK = "max_stack: ";
	private static final String MAX_LOCALS = "max_locals: ";
	private static final String ATTRIBUTES = "attributes: [";
	static final String CODE = "code: ";
	static final String BRACKET = "{";
	static final String END_BRACKET = "}";
	static final String END_SQUARE_BRACKET = "]";
	private static final String ATTRIBUTE_NAME_INDEX = "attribute_name_index: %1 (%2)";
	private static final String LENGTH = "length: ";
	private static final String CODE_LENGTH = "code_length: ";
	private static final String EXCEPTION_TABLE_LENGTH = "exception_table_length: ";
	private static final String EXCEPTION_TABLE = "exception_table: [";
	private static final String START_PC = "start_pc: ";
	private static final String END_PC = "end_pc: ";
	private static final String HANDLER_PC = "handler_pc: ";
	private static final String CATCH_TYPE_INDEX = "catch_type_index: ";
	private static final String CONSTANTVALUE_INDEX = "constantvalue_index: ";
	private static final String NUMBER_OF_EXCEPTION = "number_of_exceptions: ";
	private static final String EXCEPTION_INDEX_TABLE = "exception_index_table: [ ";
	private static final String NUMBER_OF_CLASSES = "number_of_classes: ";
	private static final String CLASSES = "classes: [";
	private static final String INNER_CLASS_INFO_INDEX = "inner_class_info_index: ";
	private static final String OUTER_CLASS_INFO_INDEX = "outer_class_info_index: ";
	private static final String INNER_NAME_INDEX = "inner_name_index: ";
	private static final String INNER_CLASS_ACCESS_FLAGS = "inner_class_access_flags: [ ";
	private static final String LINE_NUMBER_TABLE_LENGTH = "line_number_table_length: ";
	private static final String LINE_NUMBER_TABLE = "line_number_table: [";
	private static final String LINE_NUMBER = "line_number: ";
	private static final String LOCAL_VARIABLE_TABLE_LENGTH = "local_variable_table_length: ";
	private static final Object LOCAL_VARIABLE_TABLE = "local_variable_table: [";
	private static final String NAME_INDEX = "name_index: ";
	private static final String DESCRIPTOR_INDEX = "descriptor_index: ";
	private static final String INDEX = "index: ";
	private static final String SOURCE_FILE_INDEX = "sourcefile_index: ";
	private static final String BYTES = "bytes: ";

	AttributeParser(Parser parser) {
		super(parser);
	}

	void parseAttributes(int indent) {
		this.indent = indent;
		int attributeCount = Util.byteToInt(parser.readByte(2));
		println(ATTRIBUTES_COUNT + attributeCount);
		print(ATTRIBUTES);
		for (int i = 0; i < attributeCount; i++) {
			parseAttribute();
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	void parseAttribute() {
		System.out.println(BRACKET);
		int attributeNameIndex = Util.byteToInt(parser.readByte(2));
		String attributeName = parser.constantPool[attributeNameIndex];
		println(INDENT
				+ ATTRIBUTE_NAME_INDEX.replace("%1", "" + attributeNameIndex)
						.replace("%2", attributeName));
		int length = Util.byteToInt(parser.readByte(4));
		println(INDENT + LENGTH + length);
		byte[] content = parser.readByte(length);
		if (attributeName.equals(CODE_ATTR))
			parseCodeAttr(content);
		else if (attributeName.equals(CONSTANT_VALUE_ATTR))
			parseConstantValueAttr(content);
		else if (attributeName.equals(DEPRECATED_ATTR))
			parseDeprecatedAttr(content);
		else if (attributeName.equals(EXCEPTIONS_ATTR))
			parseExceptionsAttr(content);
		else if (attributeName.equals(INNER_CLASSES_ATTR))
			parseInnerClassesAttr(content);
		else if (attributeName.equals(LINE_NUMBER_TABLE_ATTR))
			parseLineNumberTableAttr(content);
		else if (attributeName.equals(LOCAL_VARIABLE_TABLE_ATTR))
			parseLocalVariableTableAttr(content);
		else if (attributeName.equals(SOURCE_FILE_ATTR))
			parseSourceFileAttr(content);
		else if (attributeName.equals(SYNTHETIC_ATTR))
			parseSyntheticAttr(content);
		else
			println(INDENT + BYTES + Util.byteToHex(content));
		print(END_BRACKET);
	}

	private void parseSyntheticAttr(byte[] content) {

	}

	private void parseSourceFileAttr(byte[] content) {
		int sourceFileIndex = Util.byteToInt(content);
		println(INDENT + SOURCE_FILE_INDEX + sourceFileIndex);
	}

	private void parseLocalVariableTableAttr(byte[] content) {
		int start = 0;
		int localVariableTableLength = Util.byteToInt(content, start, 2);
		start += 2;
		println(LOCAL_VARIABLE_TABLE_LENGTH + localVariableTableLength);
		print(LOCAL_VARIABLE_TABLE);
		while (localVariableTableLength-- > 0) {
			parseLocalVariableInfo(Arrays.copyOfRange(content, start,
					start + 10));
			start += 10;
		}
		System.out.print(END_SQUARE_BRACKET);
	}

	private void parseLocalVariableInfo(byte[] content) {
		System.out.println(BRACKET);
		int startPc = Util.byteToInt(content, 0, 2);
		println(INDENT + START_PC + startPc);
		int length = Util.byteToInt(content, 2, 2);
		println(INDENT + LENGTH + length);
		int nameIndex = Util.byteToInt(content, 4, 2);
		println(INDENT + NAME_INDEX + nameIndex);
		int descriptorIndex = Util.byteToInt(content, 6, 2);
		println(INDENT + DESCRIPTOR_INDEX + descriptorIndex);
		int index = Util.byteToInt(content, 8, 2);
		println(INDENT + INDEX + index);
		print(END_BRACKET);
	}

	private void parseLineNumberTableAttr(byte[] content) {
		int start = 0;
		int lineNumberTableLength = Util.byteToInt(content, start, 2);
		start += 2;
		println(LINE_NUMBER_TABLE_LENGTH + lineNumberTableLength);
		print(LINE_NUMBER_TABLE);
		while (lineNumberTableLength-- > 0) {
			parseLineNumberInfo(Arrays.copyOfRange(content, start, start + 4));
			start += 4;
		}
		System.out.print(END_SQUARE_BRACKET);
	}

	void parseLineNumberInfo(byte[] content) {
		System.out.println(BRACKET);
		int startPc = Util.byteToInt(content, 0, 2);
		println(INDENT + START_PC + startPc);
		int lineNumber = Util.byteToInt(content, 2, 2);
		println(INDENT + LINE_NUMBER + lineNumber);
		print(END_BRACKET);
	}

	private void parseInnerClassesAttr(byte[] content) {
		int start = 0;
		int numberOfClasses = Util.byteToInt(content, start, 2);
		start += 2;
		println(INDENT + NUMBER_OF_CLASSES + numberOfClasses);
		print(INDENT + CLASSES);
		while (numberOfClasses-- > 0) {
			parseInnerClassesInfo(Arrays.copyOfRange(content, start, start + 8));
			start += 8;
		}
		println(END_BRACKET);
	}

	void parseInnerClassesInfo(byte[] content) {
		System.out.println(BRACKET);
		int innerClassInfoIndex = Util.byteToInt(content, 0, 2);
		println(INDENT + INDENT + INNER_CLASS_INFO_INDEX + innerClassInfoIndex);
		int outerClassInfoIndex = Util.byteToInt(content, 2, 2);
		println(INDENT + INDENT + OUTER_CLASS_INFO_INDEX + outerClassInfoIndex);
		int innerNameIndex = Util.byteToInt(content, 4, 2);
		println(INDENT + INDENT + INNER_NAME_INDEX + innerNameIndex);
		int innerClassAccessFlags = Util.byteToInt(content, 6, 2);
		print(INDENT + INDENT + INNER_CLASS_ACCESS_FLAGS);
		if ((innerClassAccessFlags & Access.ACC_PUBLIC) > 0)
			System.out.print(Access.ACC_PUBLIC_STRING + " ");
		if ((innerClassAccessFlags & Access.ACC_PRIVATE) > 0)
			System.out.print(Access.ACC_PRIVATE_STRING + " ");
		if ((innerClassAccessFlags & Access.ACC_PROTECTED) > 0)
			System.out.print(Access.ACC_PROTECTED_STRING + " ");
		if ((innerClassAccessFlags & Access.ACC_STATIC) > 0)
			System.out.print(Access.ACC_STATIC_STRING + " ");
		if ((innerClassAccessFlags & Access.ACC_FINAL) > 0)
			System.out.print(Access.ACC_FINAL_STRING + " ");
		if ((innerClassAccessFlags & Access.ACC_INTERFACE) > 0)
			System.out.print(Access.ACC_INTERFACE_STRING + " ");
		println(END_SQUARE_BRACKET);
		print(INDENT + END_BRACKET);
	}

	private void parseExceptionsAttr(byte[] content) {
		int start = 0;
		int numberOfExceptions = Util.byteToInt(content, start, 2);
		start += 2;
		println(INDENT + NUMBER_OF_EXCEPTION + numberOfExceptions);
		print(INDENT + EXCEPTION_INDEX_TABLE);
		while (numberOfExceptions-- > 0) {
			int exceptionIndex = Util.byteToInt(content, start, 2);
			System.out.print(exceptionIndex + " ");
			start += 2;
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	private void parseDeprecatedAttr(byte[] content) {

	}

	private void parseConstantValueAttr(byte[] content) {
		int constantValueIndex = Util.byteToInt(content, 0, 2);
		println(INDENT + CONSTANTVALUE_INDEX + constantValueIndex);
	}

	void parseCodeAttr(byte[] content) {
		int start = 0;
		int maxStack = Util.byteToInt(content, start, 2);
		println(INDENT + MAX_STACK + maxStack);
		start += 2;
		int maxLocals = Util.byteToInt(content, start, 2);
		println(INDENT + MAX_LOCALS + maxLocals);
		start += 2;
		int codeLength = Util.byteToInt(content, start, 4);
		println(INDENT + CODE_LENGTH + codeLength);
		start += 4;
		byte[] code = Arrays.copyOfRange(content, start, start + codeLength);
		println(INDENT + CODE + BRACKET);
		for (String s : ByteCodeAnalyzer.analysisInstruction(code)) {
			println(INDENT + INDENT + s);
		}
		start += codeLength;
		int exceptionTableLength = Util.byteToInt(content, start, 2);
		println(INDENT + EXCEPTION_TABLE_LENGTH + exceptionTableLength);
		start += 2;
		print(INDENT + EXCEPTION_TABLE);
		while (exceptionTableLength-- > 0) {
			parseExceptionInfo(Arrays.copyOfRange(content, start, start + 8));
			start += 8;
		}
		System.out.println(END_SQUARE_BRACKET);
	}

	void parseExceptionInfo(byte[] content) {
		System.out.println(BRACKET);
		int startPc = Util.byteToInt(content, 0, 2);
		println(INDENT + INDENT + START_PC + startPc);
		int endPc = Util.byteToInt(content, 2, 2);
		println(INDENT + INDENT + END_PC + endPc);
		int handlerPc = Util.byteToInt(content, 4, 2);
		println(INDENT + INDENT + HANDLER_PC + handlerPc);
		int catchType = Util.byteToInt(content, 6, 2);
		println(INDENT + INDENT + CATCH_TYPE_INDEX + catchType);
		print(INDENT + END_BRACKET);
	}
}
