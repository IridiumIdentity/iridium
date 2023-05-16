import { ModuleWithProviders, NgModule } from '@angular/core';
import { NgxIridiumCallbackComponent } from './ngx-iridium-callback.component';
import { AuthorizationCodeFlowParameterService } from './service/authorization-code-flow-parameter.service';
import { UrlGeneratorService } from './service/url-generator.service';
import { AuthorizationService } from './service/authorization.service';
import { CommonModule } from '@angular/common';


@NgModule({
  declarations: [
    NgxIridiumCallbackComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    NgxIridiumCallbackComponent
  ]
})
export class NgxIridiumClientModule {}
