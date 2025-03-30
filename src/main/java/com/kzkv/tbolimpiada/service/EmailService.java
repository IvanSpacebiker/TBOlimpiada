package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;

public interface EmailService {
	void sendEmail(String toEmail, Booking booking);
}
