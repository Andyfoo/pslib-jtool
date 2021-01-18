// Copyright (c) 2013-2016, febit.org. All Rights Reserved.
package com.pslib.jtool.jftpl.support;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.febit.wit.Vars;

/**
 *
 * @author zqq90
 */
public class ServletUtil {

	public static void exportAttributes(final Map<String, Object> map, final HttpServletRequest request) {
		final Enumeration<String> enumeration = request.getAttributeNames();
		String key;
		while (enumeration.hasMoreElements()) {
			map.put(key = enumeration.nextElement(), request.getAttribute(key));
		}
	}

	public static void exportParameters(final Map<String, Object> map, final HttpServletRequest request) {
		map.putAll(request.getParameterMap());
	}

	public static Vars wrapToKeyValues(HttpServletRequest request, HttpServletResponse response) {
		return wrapToKeyValues(request, response, true, false);
	}

	public static Vars wrapToKeyValues(HttpServletRequest request, HttpServletResponse response,
			boolean exportAttributes, boolean exportParameters) {
		return new ServletVars(request, response, exportAttributes, exportParameters);
	}

	private static final class ServletVars implements Vars {

		private final HttpServletRequest request;
		private final HttpServletResponse response;
		private final boolean exportAttributes;
		private final boolean exportParameters;

		ServletVars(HttpServletRequest request, HttpServletResponse response, boolean exportAttributes,
				boolean exportParameters) {
			this.request = request;
			this.response = response;
			this.exportAttributes = exportAttributes;
			this.exportParameters = exportParameters;
		}

		@Override
		public void exportTo(final Vars.Accepter accepter) {
			final HttpServletRequest myRequest = this.request;

			accepter.set("request", myRequest);
			accepter.set("response", this.response);

			if (this.exportAttributes) {
				final Enumeration<String> enumeration = myRequest.getAttributeNames();
				String key;
				while (enumeration.hasMoreElements()) {
					accepter.set(key = enumeration.nextElement(), myRequest.getAttribute(key));
				}
			}

			if (this.exportParameters) {
				final Enumeration<String> enumeration = myRequest.getParameterNames();
				String key;
				while (enumeration.hasMoreElements()) {
					accepter.set(key = enumeration.nextElement(), myRequest.getAttribute(key));
				}
			}
		}
	}
}
