import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}
  token: string | null = null;

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (request.url.includes('auth')) {
      console.log('auth');
      return next.handle(request);
    }
    if (this.token === null) {
      this.setToken(localStorage.getItem('token'));
      if (this.token === null) {
        console.log('token null');
        return next.handle(request);
      }
    }
    const newRequest = request.clone({
      headers: request.headers.set('Authorization', this.token),
    });
    newRequest.headers.append('Authorization', this.token);
    return next.handle(newRequest);
  }

  setToken(token: string | null): void {
    this.token = token;
  }
}
