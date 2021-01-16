package com.pslib.jtool.log.log4j;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
/**
 * 过滤日志-按包名、类名
 *
 */
public class Log4jClassFilter extends Filter {
	boolean acceptOnMatch = true;
	String stringToMatch;
	String[] stringToMatchList;



	public void setStringToMatch(String s) {
		stringToMatch = s;
		stringToMatchList = s.split(",");
	}

	public String getStringToMatch() {
		return this.stringToMatch;
	}

	public void setAcceptOnMatch(boolean acceptOnMatch) {
		this.acceptOnMatch = acceptOnMatch;
	}

	public boolean getAcceptOnMatch() {
		return this.acceptOnMatch;
	}

	public int decide(LoggingEvent event) {
		String msg = event.getLocationInformation().getClassName();
		if (msg == null || stringToMatch == null || stringToMatchList == null || stringToMatchList.length == 0) {
			return NEUTRAL;
		}
		boolean isFind = false;
		for(int i = 0; i < stringToMatchList.length; i++) {
			if (msg.indexOf(stringToMatchList[i]) > -1) {
				isFind = true;
			}	
		}
		if(acceptOnMatch && isFind) {
			return ACCEPT;
		}else if(!acceptOnMatch && !isFind) {
			return ACCEPT;
		}
		return DENY;
	}
}
