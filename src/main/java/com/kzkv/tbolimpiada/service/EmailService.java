package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;
import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
	void sendEmail(String toEmail, Booking booking, HttpServletRequest request);
}
