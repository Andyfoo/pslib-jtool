package com.pslib.jtool.jftpl.loaders;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.febit.wit.loaders.Resource;

import com.pslib.jtool.jftpl.filter.TplFilter;
import com.pslib.jtool.jftpl.util.TplSkinInfo;
import com.pslib.jtool.jftpl.util.TplUtil;

public class FileResource implements Resource {

	private final String encoding;
	private TplSkinInfo skinInfo = null;

	private File file1 = null;
	private File file2 = null;

	private File file = null;
	//private long lastModified;
	private String path = null;
	protected final boolean codeFirst;

	public FileResource(String path, String encoding, boolean codeFirst, TplSkinInfo skinInfo) {
		this.encoding = encoding;
		this.skinInfo = skinInfo;
		this.codeFirst = codeFirst;

		this.path = path;
		if (skinInfo != null && skinInfo.getSkin() != null) {
			this.file1 = new File(TplUtil
					.clearPath(skinInfo.getRoot() + "/" + skinInfo.getDefaultSkin() + "/" + path));
			this.file2 = new File(
					TplUtil.clearPath(skinInfo.getRoot() + "/" + skinInfo.getSkin() + "/" + path));
		} else {
			this.file = new File(TplUtil.clearPath(skinInfo.getRoot() + "/" + path));
		}
	}

	/**
	 * @since 2.0.0
	 */
	public FileResource(String path, String encoding, boolean codeFirst) {
		this(new File(path), encoding, codeFirst);
	}

	/**
	 * @since 2.0.0
	 */
	public FileResource(File file, String encoding, boolean codeFirst) {
		this.encoding = encoding;
		this.file = file;
		this.codeFirst = codeFirst;
	}

	public FileResource(String path, String encoding) {
		this(new File(path), encoding, false);
	}

	public void routeFile() {
		if (file2 != null && file2.exists()) {
			file = file2;
		} else if (file1 != null && file1.exists()) {
			file = file1;
		}
		if (file == null) {
			file = file1;
		}
	}
//
//	public boolean isModified() {
//		routeFile();
//		return lastModified != file.lastModified();
//	}

	@Override
	public Reader openReader() throws IOException {
		routeFile();
		if (file == null) {
			throw new IOException("File is null");
		}
		try {
			//lastModified = file.lastModified();
			return new StringReader(
					TplFilter.filter(skinInfo, path, file, TplUtil.fileRead(file, encoding)));
		} catch (Exception ex) {
			throw new IOException("file error:" + file.getAbsolutePath(), ex);
		}
		// return new InputStreamReader(new FileInputStream(file),
		// encoding);
	}

	@Override
	public boolean exists() {
		routeFile();
		return file.exists();
	}

	@Override
	public boolean isCodeFirst() {
		routeFile();
		return codeFirst;
	}

	@Override
	public long version() {
		routeFile();
		return file.lastModified();
	}
}
