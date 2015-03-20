/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.executionware.agent.monitoring.impl;

import de.uniulm.omi.executionware.agent.monitoring.api.Measurement;
import de.uniulm.omi.executionware.agent.monitoring.api.Metric;

/**
 * Created by daniel on 06.02.15.
 */
public class MetricFactory {

    private MetricFactory() {

    }

    public static Metric from(String metricName, Measurement measurement, MonitorContext monitorContext) {
        return MetricBuilder.newBuilder().name(metricName).value(measurement.getValue()).timestamp(measurement.getTimestamp()).addTags(monitorContext.getContext()).build();
    }

}
