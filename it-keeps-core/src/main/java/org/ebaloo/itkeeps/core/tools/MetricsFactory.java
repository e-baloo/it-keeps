/*
 * Copyright (c) 2001-2016 Group JCDecaux.
 * 17 rue Soyer, 92523 Neuilly Cedex, France.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Group JCDecaux ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you
 * entered into with Group JCDecaux.
 */

package org.ebaloo.itkeeps.core.tools;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

public class MetricsFactory {

	
	
	
	private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

	private static final String PROP_METRIC_REG_JVM_MEMORY = "jvm.memory";
	private static final String PROP_METRIC_REG_JVM_GARBAGE = "jvm.garbage";
	private static final String PROP_METRIC_REG_JVM_THREADS = "jvm.threads";
	private static final String PROP_METRIC_REG_JVM_FILES = "jvm.files";
	private static final String PROP_METRIC_REG_JVM_BUFFERS = "jvm.buffers";

	public static void enableConsole() {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(METRIC_REGISTRY).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(15, TimeUnit.SECONDS);
	}

	private static Boolean bEnableJvm = false;

	public static void enableJvm() {

		if (bEnableJvm)
			return;

		METRIC_REGISTRY.register(PROP_METRIC_REG_JVM_MEMORY, new MemoryUsageGaugeSet());
		METRIC_REGISTRY.register(PROP_METRIC_REG_JVM_GARBAGE, new GarbageCollectorMetricSet());
		METRIC_REGISTRY.register(PROP_METRIC_REG_JVM_THREADS, new ThreadStatesGaugeSet());
		METRIC_REGISTRY.register(PROP_METRIC_REG_JVM_FILES, new FileDescriptorRatioGauge());
		METRIC_REGISTRY.register(PROP_METRIC_REG_JVM_BUFFERS,
				new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));

		final JmxReporter jmxReporter = JmxReporter.forRegistry(METRIC_REGISTRY).build();
		jmxReporter.start();

		bEnableJvm = true;
	}

	public static MetricRegistry getMetricRegistry() {
		return METRIC_REGISTRY;
	}

}
