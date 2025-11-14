package com.foodcourt.foodcourt.domain.gateways;

public interface NotificationServiceGateway {
	
	void sendOrderCompletedNotification(String phoneNumber, String securePin);
	
}
