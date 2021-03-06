/*
 * Copyright (C) 2009-2010 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.preferences;

import java.util.Map;
import java.util.TreeMap;

import android.content.Context;

import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.language.ZLLanguageList;

import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;

import org.geometerplus.fbreader.fbreader.FBReader;
import org.geometerplus.fbreader.library.Book;

class BookTitlePreference extends ZLStringPreference {
	private final Book myBook;

	BookTitlePreference(Context context, ZLResource rootResource, String resourceKey, Book book) {
		super(context, rootResource, resourceKey);
		myBook = book;
		setValue(book.getTitle());
	}

	public void onAccept() {
		myBook.setTitle(getValue());
	}
}

class LanguagePreference extends ZLStringListPreference {
	private final Book myBook;

	LanguagePreference(Context context, ZLResource rootResource, String resourceKey, Book book) {
		super(context, rootResource.getResource(resourceKey), null);
		myBook = book;
		final TreeMap<String,String> map = new TreeMap<String,String>();
		for (String code : ZLLanguageList.languageCodes()) {
			map.put(ZLLanguageList.languageName(code), code);
		}
		final int size = map.size();
		String[] codes = new String[size + 1];
		String[] names = new String[size + 1];
		int index = 0;
		for (Map.Entry<String,String> entry : map.entrySet()) {
			codes[index] = entry.getValue();
			names[index] = entry.getKey();
			++index;
		}
		codes[size] = "other";
		names[size] = ZLLanguageList.languageName(codes[size]);
		setLists(codes, names);
		String language = myBook.getLanguage();
		if (language == null) {
			language = "other";
		}
		if (!setInitialValue(language)) {
			setInitialValue("other");
		}
	}

	public void onAccept() {
		final String value = getValue();
		myBook.setLanguage((value.length() != 0) ? value : null);
	}
}

public class BookInfoActivity extends ZLPreferenceActivity {
	private Book myBook;

	public BookInfoActivity() {
		super("BookInfo");
	}

	@Override
	protected void init() {
		final Category commonCategory = createCategory(null);
		myBook = ((FBReader)FBReader.Instance()).Model.Book;
		if (myBook.File.getPhysicalFile() != null) {
			commonCategory.addPreference(new InfoPreference(
				this,
				commonCategory.Resource.getResource("fileName").getValue(),
				myBook.File.getPath())
			);
		}
		commonCategory.addPreference(new BookTitlePreference(this, commonCategory.Resource, "title", myBook));
		commonCategory.addPreference(new LanguagePreference(this, commonCategory.Resource, "language", myBook));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (myBook.save()) {
			((FBReader)FBReader.Instance()).clearTextCaches();
			ZLTextHyphenator.Instance().load(myBook.getLanguage());
		}
	}
}
