package com.pslib.jtool.jftpl.config;

import org.febit.wit.Context;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;
import org.febit.wit.lang.MethodDeclare;
import org.febit.wit.util.ArrayUtil;

public class TypeGlobalRegister implements GlobalRegister {

	@Override
	public void regist(GlobalManager manager) {
		manager.setConstMethod("is_array", TypeGlobalRegister::is_array);
		manager.setConstMethod("is_bool", TypeGlobalRegister::is_bool);
		manager.setConstMethod("is_function", TypeGlobalRegister::is_function);
		manager.setConstMethod("is_callable", TypeGlobalRegister::is_function);
		manager.setConstMethod("is_null", TypeGlobalRegister::is_null);
		manager.setConstMethod("is_number", TypeGlobalRegister::is_number);
	}

	private static boolean is_function(Context context, Object[] args) {
		return ArrayUtil.get(args, 0) instanceof MethodDeclare;
	}

	private static boolean is_number(Context context, Object[] args) {
		return ArrayUtil.get(args, 0) instanceof Number;
	}

	private static boolean is_bool(Context context, Object[] args) {
		return ArrayUtil.get(args, 0) instanceof Boolean;
	}

	private static boolean is_null(Context context, Object[] args) {
		return args.length == 0 || ArrayUtil.get(args, 0) == null;
	}

	private static boolean is_array(Context context, Object[] args) {
		final Object item = ArrayUtil.get(args, 0);
		return item != null && item.getClass().isArray();
	}
}