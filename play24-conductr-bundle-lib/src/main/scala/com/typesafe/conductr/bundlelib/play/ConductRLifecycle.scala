/*
 * Copyright © 2014-2015 Typesafe, Inc. All rights reserved.
 * No information contained herein may be reproduced or transmitted in any form
 * or by any means without the express written permission of Typesafe, Inc.
 */

package com.typesafe.conductr.bundlelib.play

import com.typesafe.conductr.bundlelib.play.ConnectionContext.Implicits
import play.api.inject.{ Binding, Module }
import play.api.{ Environment, Configuration, Logger }
import javax.inject.Singleton

/**
 * Takes care of managing ConductR lifecycle events. In order to enable
 * ConductR lifecycle events for your application, add the following to
 * your application.conf:
 *
 *   play.modules.enabled += "com.typesafe.conductr.bundlelib.play.ConductRLifecycleModule"
 */
class ConductRLifecycleModule extends Module {

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] =
    Seq(
      bind(classOf[ConductRLifecycle]).toSelf.eagerly()
    )
}

/**
 * Responsible for signalling ConductR that the application has started.
 */
@Singleton
class ConductRLifecycle {

  import Implicits.defaultContext

  StatusService.signalStartedOrExit()
  Logger.info("Signalled start to ConductR")
}
