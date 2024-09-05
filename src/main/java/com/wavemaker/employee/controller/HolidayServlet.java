package com.wavemaker.employee.controller;

import com.google.gson.Gson;
import com.wavemaker.employee.exception.ErrorResponse;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.Holiday;
import com.wavemaker.employee.service.HolidayService;
import com.wavemaker.employee.service.impl.HolidayServiceImpl;
import com.wavemaker.employee.util.ClientResponseHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@WebServlet("/employee/holiday")
public class HolidayServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HolidayServlet.class);
    private Gson gson = null;
    private HolidayService holidayService = null;

    @Override
    public void init(ServletConfig config) {
        gson = new Gson();
        holidayService = new HolidayServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
        Holiday holiday = null;
        List<Holiday> holidayList = null;
        String jsonResponse = null;
        String holidayId = request.getParameter("holidayId");
        try {
            if (holidayId != null) {
                holiday = holidayService.getHolidayById(Integer.parseInt(holidayId));
                jsonResponse = gson.toJson(holiday);
                ClientResponseHandler.sendResponseToClient(response,jsonResponse,logger);
            }

            holidayList = holidayService.getUpcommingHolidayList();
            jsonResponse = gson.toJson(holidayList);
        }catch (ServerUnavilableException e) {
            ErrorResponse errorResponse = new ErrorResponse("Error occurred while retrieving leave types", 500);
            jsonResponse = gson.toJson(errorResponse);
            response.setStatus(500);
        } finally {
            holidayId = null;
            holiday = null;
            holidayList = null;
            ClientResponseHandler.sendResponseToClient(response, jsonResponse, logger);
        }
    }
}
