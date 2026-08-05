package com.xiaoyu.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyu.ai.entity.Appointment;

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
