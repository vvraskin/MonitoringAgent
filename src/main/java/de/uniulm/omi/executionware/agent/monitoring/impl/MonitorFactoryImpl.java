/*
 *
 *  * Copyright (c) 2014 University of Ulm
 *  *
 *  * See the NOTICE file distributed with this work for additional information
 *  * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package de.uniulm.omi.executionware.agent.monitoring.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.uniulm.omi.executionware.agent.monitoring.api.*;
import de.uniulm.omi.executionware.agent.reporting.api.ReportingInterface;
import de.uniulm.omi.executionware.agent.reporting.modules.api.QueuedReporting;

import java.util.Map;

/**
 * Created by daniel on 15.01.15.
 */
public class
        MonitorFactoryImpl implements MonitorFactory {

    private final String localIp;
    private final ReportingInterface<Metric> metricReportingInterface;

    @Inject
    public MonitorFactoryImpl(@Named("localIp") String localIp, @QueuedReporting ReportingInterface<Metric> metricReportingInterface) {
        this.localIp = localIp;
        this.metricReportingInterface = metricReportingInterface;
    }

    @Override
    public Monitor create(String metricName, Sensor sensor, Interval interval, Map<String, String> context) throws InvalidMonitorContextException {
        MonitorContext monitorContext = MonitorContext.builder().addContext(MonitorContext.LOCAL_IP, localIp).addContext(context).build();
        return new MonitorImpl(metricName, sensor, interval, monitorContext, metricReportingInterface);
    }
}
