import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { NgxIridiumClientService } from 'ngx-iridium-client';
import { tap } from 'rxjs';

export const authGuard = async () => {
  const router = inject(Router);
  const authService = inject(NgxIridiumClientService);
  console.log('in auth guard')
  authService.isAuthenticated().subscribe({
    next: (value) => {
      console.log('next', value)
      return true;
    },
    error: (error) => {
      console.error('error', error)
      router.navigateByUrl('/')
    },
  })
};
