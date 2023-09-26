import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { Order } from '../type';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor(private http: HttpClient) {}

  httpOptions = {
    headers: new HttpHeaders().append('Content-Type', 'application/json'),
  };

  private orderUrl = 'http://localhost:8080/orders';

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  updateOrder(order: Order): Observable<any> {
    return this.http
      .put(this.orderUrl, order, this.httpOptions)
      .pipe(catchError(this.handleError(`updateOrder`)));
  }

  getOrder(id: number): Observable<any> {
    const url = `${this.orderUrl}/${id}`;
    return this.http
      .get(url)
      .pipe(catchError(this.handleError(`getProduct id:${id}`)));
  }

  getAllOrders(): Observable<any> {
    return this.http.get(this.orderUrl);
  }

  getOrdersofUser(): Observable<any> {
    const url = `${this.orderUrl}/user`;
    return this.http
      .get(url)
      .pipe(catchError(this.handleError(`getOrdersofUser`)));
  }

  createOrder(order: Order): Observable<any> {
    return this.http
      .post(this.orderUrl, order, this.httpOptions)
      .pipe(catchError(this.handleError(`createOrder`)));
  }
}
