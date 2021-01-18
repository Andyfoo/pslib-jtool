package com.pslib.jtool.jftpl.loaders;

import org.febit.wit.loaders.AbstractLoader;
import org.febit.wit.loaders.Resource;
import com.pslib.jtool.jftpl.util.TplSkinInfo;

public class FileLoader extends AbstractLoader {

	@Override
	public Resource get(String name) {
		String[] nameArr = name.split("\\|");
		name = nameArr[0];
		// System.out.println(nameArr[1]);
		TplSkinInfo skinInfo = TplSkinInfo.parse(nameArr[1]);
		skinInfo.setRoot(root);
		return new FileResource(name, encoding, codeFirst, skinInfo);

		// return new FileResource(getRealPath(name), encoding,
		// codeFirst, null);
	}
}
