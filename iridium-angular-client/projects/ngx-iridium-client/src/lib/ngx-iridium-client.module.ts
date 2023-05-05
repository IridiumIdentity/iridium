import { ModuleWithProviders, NgModule } from '@angular/core';
import { NgxIridiumClientComponent } from './ngx-iridium-client.component';
import { AuthorizationCodeFlowParameterService } from './service/authorization-code-flow-parameter.service';
import { UrlGeneratorService } from './service/url-generator.service';
import { AuthorizationService } from './service/authorization.service';



@NgModule({
  declarations: [
    NgxIridiumClientComponent
  ],
  imports: [
  ],
  exports: [
    NgxIridiumClientComponent
  ]
})
export class NgxIridiumClientModule {
  static forRoot(configuration: any): ModuleWithProviders<any> {
    console.log(configuration);
    return {
      ngModule: NgxIridiumClientModule,
      providers: [
        AuthorizationCodeFlowParameterService,{provide: 'config', useValue: configuration},
        UrlGeneratorService,{provide: 'config', useValue: configuration},
        AuthorizationService,{provide: 'config', useValue: configuration}
      ]
    };
  }
}
