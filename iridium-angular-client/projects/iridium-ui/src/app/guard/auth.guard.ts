import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { NgxIridiumClientService } from 'ngx-iridium-client';

export const authGuard = async () => {
  const router = inject(Router);
  const authService = inject(NgxIridiumClientService);

  const result = (await authService.isAuthenticated())
    .subscribe((result) => {
        console.log('subscribe result', result)
    });
  console.log('auth result is, ', result)
  if (result) {
    return true;
  }
  return router.createUrlTree(['/']);
};
