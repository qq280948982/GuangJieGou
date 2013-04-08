package com.tim.guangjiegou.service;

import java.util.HashSet;
import java.util.Set;

public class NotificationCenter {
	
	private static Set<NotificationObserver> sNotificationMap = new HashSet<NotificationObserver>();
	
	public interface NotificationObserver {
		void onReceiveNotification(int notificationCode);
	}
	
	public static void registerNotification(NotificationObserver observer) {
		sNotificationMap.add(observer);
	}
	
	public static void unregisterNotification(NotificationObserver observer) {
		sNotificationMap.remove(observer);
	}
	
	public static void sendNotification(int notificationCode) {
		for(NotificationObserver observer : sNotificationMap) {
			observer.onReceiveNotification(notificationCode);
		}
	}

}
