package org.geometerplus.zlibrary.text.model.impl;

import org.geometerplus.zlibrary.text.model.ZLTextModel;
import org.geometerplus.zlibrary.text.model.ZLTextParagraph;
import org.geometerplus.zlibrary.text.model.ZLTextPlainModel;
import org.geometerplus.zlibrary.text.model.ZLTextTreeModel;
import org.geometerplus.zlibrary.text.model.ZLTextTreeParagraph;


public class ZLModelFactory {
	//models
	static public ZLTextPlainModel createPlainModel(int dataBlockSize) {
		return new ZLTextPlainModelImpl(dataBlockSize);
	}
	
	/*static public ZLTextPlainModel createPlainModel() {
		return new ZLTextPlainModelImpl();
	}*/
	
	static public ZLTextTreeModel createZLTextTreeModel() {
		return new ZLTextTreeModelImpl();
	}

	//paragraphs
	static public ZLTextParagraph createParagraph() {
		return new ZLTextParagraphImpl(new ZLTextPlainModelImpl(4096));
	}
	
	static public ZLTextParagraph createSpecialParagragraph(byte kind) {
		return new ZLTextSpecialParagraphImpl(kind, new ZLTextPlainModelImpl(4096));
	}
	
	static public ZLTextTreeParagraph createTreeParagraph(ZLTextTreeParagraph parent) {
		return new ZLTextTreeParagraphImpl(parent, new ZLTextPlainModelImpl(4096));
	}
	
	static public ZLTextTreeParagraph createTreeParagraph() {
		return new ZLTextTreeParagraphImpl(null, new ZLTextPlainModelImpl(4096));
	}

	//entries
	static public ZLTextForcedControlEntry createForcedControlEntry() {
		return new ZLTextForcedControlEntryImpl();
	}
}