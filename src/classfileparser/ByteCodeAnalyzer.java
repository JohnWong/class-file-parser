package classfileparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Instruction {
	String mnemonic;
	int numberOfOperand;

	public Instruction(String mnemonic, int numberOfOperand) {
		this.mnemonic = mnemonic;
		this.numberOfOperand = numberOfOperand;
	}
}

public class ByteCodeAnalyzer {

	private static final Map<Integer, Instruction> OP_MAP = new HashMap<Integer, Instruction>();
	static {
		OP_MAP.put(0x32, new Instruction("AALOAD", 1));
		OP_MAP.put(0X53, new Instruction("AASTORE", 2));
		OP_MAP.put(0X1, new Instruction("ACONST_NULL", 0));
		OP_MAP.put(0X19, new Instruction("ALOAD", 1));
		OP_MAP.put(0X2A, new Instruction("ALOAD_0", 0));
		OP_MAP.put(0X2B, new Instruction("ALOAD_1", 0));
		OP_MAP.put(0X2C, new Instruction("ALOAD_2", 0));
		OP_MAP.put(0X2D, new Instruction("ALOAD_3", 0));
		OP_MAP.put(0XBD, new Instruction("ANEWARRAY", 2));
		OP_MAP.put(0XB0, new Instruction("ARETURN", 0));
		OP_MAP.put(0XBE, new Instruction("ARRAYLENGTH", 0));
		OP_MAP.put(0X3A, new Instruction("ASTORE", 1));
		OP_MAP.put(0X4B, new Instruction("ASTORE_0", 0));
		OP_MAP.put(0X4C, new Instruction("ASTORE_1", 0));
		OP_MAP.put(0X4D, new Instruction("ASTORE_2", 0));
		OP_MAP.put(0X4E, new Instruction("ASTORE_3", 0));
		OP_MAP.put(0XBF, new Instruction("ATHROW", 0));
		OP_MAP.put(0X33, new Instruction("BALOAD", 0));
		OP_MAP.put(0X54, new Instruction("BASTORE", 0));
		OP_MAP.put(0X10, new Instruction("BIPUSH", 1));
		OP_MAP.put(0X34, new Instruction("CALOAD", 0));
		OP_MAP.put(0X55, new Instruction("CASTORE", 0));
		OP_MAP.put(0XC0, new Instruction("CHECKCAST", 2));
		OP_MAP.put(0X90, new Instruction("D2F", 0));
		OP_MAP.put(0X8E, new Instruction("D2I", 0));
		OP_MAP.put(0X8F, new Instruction("D2L", 0));
		OP_MAP.put(0X63, new Instruction("DADD", 0));
		OP_MAP.put(0X31, new Instruction("DALOAD", 0));
		OP_MAP.put(0X52, new Instruction("DASTORE", 0));
		OP_MAP.put(0X98, new Instruction("DCMPG", 0));
		OP_MAP.put(0X97, new Instruction("DCMPL", 0));
		OP_MAP.put(0XE, new Instruction("DCONST_0", 0));
		OP_MAP.put(0XF, new Instruction("DCONST_1", 0));
		OP_MAP.put(0X6F, new Instruction("DDIV", 0));
		OP_MAP.put(0X18, new Instruction("DLOAD", 1));
		OP_MAP.put(0X26, new Instruction("DLOAD_0", 0));
		OP_MAP.put(0X27, new Instruction("DLOAD_1", 0));
		OP_MAP.put(0X28, new Instruction("DLOAD_2", 0));
		OP_MAP.put(0X29, new Instruction("DLOAD_3", 0));
		OP_MAP.put(0X6B, new Instruction("DMUL", 0));
		OP_MAP.put(0x77, new Instruction("DNEG", 0));
		OP_MAP.put(0X73, new Instruction("DREM", 0));
		OP_MAP.put(0XAF, new Instruction("DRETURN", 0));
		OP_MAP.put(0X39, new Instruction("DSTORE", 1));
		OP_MAP.put(0X47, new Instruction("DSTORE_0", 0));
		OP_MAP.put(0X48, new Instruction("DSTORE_1", 0));
		OP_MAP.put(0X49, new Instruction("DSTORE_2", 0));
		OP_MAP.put(0X4A, new Instruction("DSTORE_3", 0));
		OP_MAP.put(0X67, new Instruction("DSUB", 0));
		OP_MAP.put(0X59, new Instruction("DUP", 0));
		OP_MAP.put(0X5A, new Instruction("DUX_X1", 0));
		OP_MAP.put(0X5B, new Instruction("DUX_X2", 0));
		OP_MAP.put(0X5C, new Instruction("DUP2", 0));
		OP_MAP.put(0X5D, new Instruction("DUP2_X1", 0));
		OP_MAP.put(0X5E, new Instruction("DUP2_X2", 0));
		OP_MAP.put(0X8D, new Instruction("F2D", 0));
		OP_MAP.put(0X8B, new Instruction("F2I", 0));
		OP_MAP.put(0X8C, new Instruction("F2L", 0));
		OP_MAP.put(0X62, new Instruction("FADD", 0));
		OP_MAP.put(0X30, new Instruction("FALOAD", 0));
		OP_MAP.put(0X51, new Instruction("FASTORE", 0));
		OP_MAP.put(0X96, new Instruction("FCMPG", 0));
		OP_MAP.put(0X95, new Instruction("FCMPL", 0));
		OP_MAP.put(0XB, new Instruction("FCONST_0", 0));
		OP_MAP.put(0XC, new Instruction("FCONST_1", 0));
		OP_MAP.put(0XD, new Instruction("FCONST_2", 0));
		OP_MAP.put(0X6E, new Instruction("FDIV", 0));
		OP_MAP.put(0X17, new Instruction("FLOAD", 1));
		OP_MAP.put(0X22, new Instruction("FLOAD_0", 0));
		OP_MAP.put(0X23, new Instruction("FLOAD_1", 0));
		OP_MAP.put(0X24, new Instruction("FLOAD_2", 0));
		OP_MAP.put(0X25, new Instruction("FLOAD_3", 0));
		OP_MAP.put(0X6A, new Instruction("FMUL", 0));
		OP_MAP.put(0X76, new Instruction("FNEG", 0));
		OP_MAP.put(0X72, new Instruction("FREM", 0));
		OP_MAP.put(0XAE, new Instruction("FRETURN", 0));
		OP_MAP.put(0X38, new Instruction("FSTORE", 1));
		OP_MAP.put(0X43, new Instruction("FSTORE_0", 0));
		OP_MAP.put(0X44, new Instruction("FSTORE_1", 0));
		OP_MAP.put(0X45, new Instruction("FSTORE_2", 0));
		OP_MAP.put(0X46, new Instruction("FSTORE_3", 0));
		OP_MAP.put(0X66, new Instruction("FSUB", 0));
		OP_MAP.put(0XB4, new Instruction("GETFIELD", 2));
		OP_MAP.put(0XB2, new Instruction("GETSTATIC", 2));
		OP_MAP.put(0XA7, new Instruction("GOTO", 2));
		OP_MAP.put(0XC8, new Instruction("GOTO_W", 4));
		OP_MAP.put(0X91, new Instruction("I2B", 0));
		OP_MAP.put(0X92, new Instruction("I2C", 0));
		OP_MAP.put(0X87, new Instruction("I2D", 0));
		OP_MAP.put(0X86, new Instruction("I2F", 0));
		OP_MAP.put(0X85, new Instruction("I2L", 0));
		OP_MAP.put(0X93, new Instruction("I2S", 0));
		OP_MAP.put(0X60, new Instruction("IADD", 0));
		OP_MAP.put(0X2E, new Instruction("IALOAD", 0));
		OP_MAP.put(0X7E, new Instruction("IAND", 0));
		OP_MAP.put(0X4F, new Instruction("IASTORE", 0));
		OP_MAP.put(0X3, new Instruction("ICONST_0", 0));
		OP_MAP.put(0X4, new Instruction("ICONST_1", 0));
		OP_MAP.put(0X5, new Instruction("ICONST_2", 0));
		OP_MAP.put(0X6, new Instruction("ICONST_3", 0));
		OP_MAP.put(0X7, new Instruction("ICONST_4", 0));
		OP_MAP.put(0X8, new Instruction("ICONST_5", 0));
		OP_MAP.put(0X2, new Instruction("ICONST_M1", 0));
		OP_MAP.put(0X6C, new Instruction("IDIV", 0));
		OP_MAP.put(0X99, new Instruction("IFEQ", 2));
		OP_MAP.put(0X9C, new Instruction("IFGE", 2));
		OP_MAP.put(0X9D, new Instruction("IFGT", 2));
		OP_MAP.put(0X9E, new Instruction("IFLE", 2));
		OP_MAP.put(0X9B, new Instruction("IFLT", 2));
		OP_MAP.put(0X9A, new Instruction("IFNE", 2));
		OP_MAP.put(0XC7, new Instruction("IFNONNULL", 2));
		OP_MAP.put(0XC6, new Instruction("IFNULL", 2));
		OP_MAP.put(0XA5, new Instruction("IF_ACMPEQ", 2));
		OP_MAP.put(0XA6, new Instruction("IF_ACMPNE", 2));
		OP_MAP.put(0X9F, new Instruction("IF_ICMPEQ", 2));
		OP_MAP.put(0XA2, new Instruction("IF_ICMPGE", 2));
		OP_MAP.put(0XA3, new Instruction("IF_ICMPGT", 2));
		OP_MAP.put(0XA4, new Instruction("IF_ICMPLE", 2));
		OP_MAP.put(0XA1, new Instruction("IF_ICMPLT", 2));
		OP_MAP.put(0XA0, new Instruction("IF_ICMPNE", 2));
		OP_MAP.put(0X84, new Instruction("IINC", 2));
		OP_MAP.put(0X15, new Instruction("ILOAD", 1));
		OP_MAP.put(0X1A, new Instruction("ILOAD_0", 0));
		OP_MAP.put(0X1B, new Instruction("ILOAD_1", 0));
		OP_MAP.put(0X1C, new Instruction("ILOAD_2", 0));
		OP_MAP.put(0X1D, new Instruction("ILOAD_3", 0));
		OP_MAP.put(0X68, new Instruction("IMUL", 0));
		OP_MAP.put(0X74, new Instruction("INEG", 0));
		OP_MAP.put(0XC1, new Instruction("INSTANCEOF", 2));
		OP_MAP.put(0XB9, new Instruction("INVOKEintERFACE", 4));
		OP_MAP.put(0XB7, new Instruction("INVOKESPECIAL", 2));
		OP_MAP.put(0XB8, new Instruction("INVOKESTATIC", 2));
		OP_MAP.put(0XB6, new Instruction("INVOKEVIRTUAL", 2));
		OP_MAP.put(0X80, new Instruction("IOR", 0));
		OP_MAP.put(0X70, new Instruction("IREM", 0));
		OP_MAP.put(0XAC, new Instruction("IRETURN", 0));
		OP_MAP.put(0X78, new Instruction("ISHL", 0));
		OP_MAP.put(0X7A, new Instruction("ISHR", 0));
		OP_MAP.put(0X36, new Instruction("ISTORE", 1));
		OP_MAP.put(0X3B, new Instruction("ISTORE_0", 0));
		OP_MAP.put(0X3C, new Instruction("ISTORE_1", 0));
		OP_MAP.put(0X3D, new Instruction("ISTORE_2", 0));
		OP_MAP.put(0X3E, new Instruction("ISTORE_3", 0));
		OP_MAP.put(0X64, new Instruction("ISUB", 0));
		OP_MAP.put(0X7C, new Instruction("IUSHR", 0));
		OP_MAP.put(0X82, new Instruction("IXOR", 0));
		OP_MAP.put(0XA8, new Instruction("JSR", 2));
		OP_MAP.put(0XC9, new Instruction("JSR_W", 4));
		OP_MAP.put(0X8A, new Instruction("L2D", 0));
		OP_MAP.put(0X89, new Instruction("L2F", 0));
		OP_MAP.put(0X88, new Instruction("L2I", 0));
		OP_MAP.put(0X61, new Instruction("LADD", 0));
		OP_MAP.put(0X2F, new Instruction("LALOAD", 0));
		OP_MAP.put(0X7F, new Instruction("LAND", 0));
		OP_MAP.put(0X50, new Instruction("LASTORE", 0));
		OP_MAP.put(0X94, new Instruction("LCMP", 0));
		OP_MAP.put(0X9, new Instruction("LCONST_0", 0));
		OP_MAP.put(0XA, new Instruction("LCONST_1", 0));
		OP_MAP.put(0X12, new Instruction("LDC", 1));
		OP_MAP.put(0X13, new Instruction("LDC_W", 2));
		OP_MAP.put(0X14, new Instruction("LDC2_W", 2));
		OP_MAP.put(0X6D, new Instruction("LDIV", 0));
		OP_MAP.put(0X16, new Instruction("LLOAD", 1));
		OP_MAP.put(0X1E, new Instruction("LLOAD_0", 0));
		OP_MAP.put(0X1F, new Instruction("LLOAD_1", 0));
		OP_MAP.put(0X20, new Instruction("LLOAD_2", 0));
		OP_MAP.put(0X21, new Instruction("LLOAD_3", 0));
		OP_MAP.put(0X69, new Instruction("LMUL", 0));
		OP_MAP.put(0X75, new Instruction("LNEG", 0));
		OP_MAP.put(0XAB, new Instruction("LOOKUPSWITCH", -1));//
		OP_MAP.put(0X81, new Instruction("LOR", 0));
		OP_MAP.put(0X71, new Instruction("LREM", 0));
		OP_MAP.put(0XAD, new Instruction("LRETURN", 0));
		OP_MAP.put(0X79, new Instruction("LSHL", 0));
		OP_MAP.put(0X7B, new Instruction("LSHR", 0));
		OP_MAP.put(0X37, new Instruction("LSTORE", 1));
		OP_MAP.put(0X3F, new Instruction("LSTORE_0", 0));
		OP_MAP.put(0X40, new Instruction("LSTORE_1", 0));
		OP_MAP.put(0X41, new Instruction("LSTORE_2", 0));
		OP_MAP.put(0X42, new Instruction("LSTORE_3", 0));
		OP_MAP.put(0X65, new Instruction("LSUB", 0));
		OP_MAP.put(0X7D, new Instruction("LUSHR", 0));
		OP_MAP.put(0X83, new Instruction("LXOR", 0));
		OP_MAP.put(0XC2, new Instruction("MONITORENTER", 0));
		OP_MAP.put(0XC3, new Instruction("MONITOREXIT", 0));
		OP_MAP.put(0XC5, new Instruction("MULTIANEWARRAY", 3));
		OP_MAP.put(0XBB, new Instruction("NEW", 2));
		OP_MAP.put(0XBC, new Instruction("NEWARRAY", 1));
		OP_MAP.put(0X0, new Instruction("NOP", 0));
		OP_MAP.put(0X57, new Instruction("POP", 0));
		OP_MAP.put(0X58, new Instruction("POP2", 0));
		OP_MAP.put(0XB5, new Instruction("PUTFIELD", 2));
		OP_MAP.put(0XB3, new Instruction("PUTSTATIC", 2));
		OP_MAP.put(0XA9, new Instruction("RET", 1));
		OP_MAP.put(0XB1, new Instruction("RETURN", 0));
		OP_MAP.put(0X35, new Instruction("SALOAD", 0));
		OP_MAP.put(0X56, new Instruction("SASTORE", 0));
		OP_MAP.put(0X11, new Instruction("SIPUSH", 2));
		OP_MAP.put(0X5F, new Instruction("SWAP", 0));
		OP_MAP.put(0XAA, new Instruction("TABLESWITCH", -2));//
		/*
		 * wide, <opcode>, indexbyte1, indexbyte2 or wide, iinc, indexbyte1,
		 * indexbyte2, constbyte1, constbyte2
		 */
		OP_MAP.put(0XC4, new Instruction("WIDE", -3));//
	}

	private static Instruction getOp(byte opCode) {
		Integer intOp = 0xff & opCode;
		return OP_MAP.get(intOp);
	}

	static public String[] analysisInstruction(byte[] content) {
		List<String> instructionList = new ArrayList<String>();
		for (int i = 0; i < content.length;) {
			byte b = content[i++];
			Instruction instruction = getOp(b);
			if (instruction.numberOfOperand >= 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(instruction.mnemonic);
				for (int j = 0; j < instruction.numberOfOperand; j++) {
					sb.append(" ");
					sb.append(Util.byteToHex(content[i++]));
				}
				instructionList.add(sb.toString());
			} else if (instruction.numberOfOperand == -1) {
				// look_up_switch
				StringBuilder sb = new StringBuilder();
				sb.append(instruction.mnemonic);
				int pad = i % 4;
				if (pad != 0)
					i += 4 - pad;
				int defaultBytes = Util.byteToInt(content, i, 4);
				i += 4;
				sb.append(" ");
				sb.append(defaultBytes);
				int npairs = Util.byteToInt(content, i, 4);
				i += 4;
				sb.append(" ");
				sb.append(npairs);
				sb.append(" {");
				for (int j = 0; j < npairs; j++) {
					if (j != 0)
						sb.append(", ");
					int key = Util.byteToInt(content, i, 4);
					i += 4;
					int value = Util.byteToInt(content, i, 4);
					i += 4;
					sb.append(key);
					sb.append(": ");
					sb.append(value);
				}
				sb.append("}");
				instructionList.add(sb.toString());
			} else if (instruction.numberOfOperand == -2) {
				// table_switch
				StringBuilder sb = new StringBuilder();
				sb.append(instruction.mnemonic);
				int pad = i % 4;
				if (pad != 0)
					i += 4 - pad;
				int defaultBytes = Util.byteToInt(content, i, 4);
				i += 4;
				sb.append(" ");
				sb.append(defaultBytes);
				int lowBytes = Util.byteToInt(content, i, 4);
				i += 4;
				sb.append(" ");
				sb.append(lowBytes);
				int highBytes = Util.byteToInt(content, i, 4);
				i += 4;
				sb.append(" ");
				sb.append(highBytes);
				sb.append(" {");
				for (int j = 0; j < highBytes - lowBytes + 1; j++) {
					if (j != 0)
						sb.append(", ");
					int branchOffsets = Util.byteToInt(content, i, 4);
					i += 4;
					sb.append(" ");
					sb.append(branchOffsets);
				}
				sb.append("}");
				instructionList.add(sb.toString());
			} else if (instruction.numberOfOperand == -3) {
				StringBuilder sb = new StringBuilder();
				sb.append(instruction.mnemonic);
				sb.append(" ");
				byte opcode = content[i];
				sb.append(getOp(content[i++]).mnemonic);
				sb.append(" ");
				sb.append(Util.byteToInt(content, i, 2));
				i += 2;
				if (opcode == 0X84) {
					sb.append(" ");
					sb.append(" ");
					sb.append(Util.byteToInt(content, i, 2));
					i += 2;
				}
				instructionList.add(sb.toString());

			} else {
				System.out.println("Error: " + instruction.numberOfOperand);
			}
		}
		return instructionList.toArray(new String[] {});
	}
}
