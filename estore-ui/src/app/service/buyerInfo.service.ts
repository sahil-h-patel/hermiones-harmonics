import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { BuyerInfo, Cart } from '.././type';

@Injectable({
  providedIn: 'root',
})
export class BuyerInfoService {
  constructor(private http: HttpClient) {}

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  private buyerInformationURL = 'http://localhost:8080/buyerInformation';

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  getBuyerInfos(): Observable<Array<BuyerInfo>> {
    return this.http.get<Array<BuyerInfo>>(this.buyerInformationURL);
  }

  getBuyerInfo(id: number): Observable<BuyerInfo> {
    const url = `${this.buyerInformationURL}/${id}`;
    return this.http
      .get<BuyerInfo>(url)
      .pipe(catchError(this.handleError<any>(`getBuyerInfo id:${id}`)));
  }

  createNewBuyerInfo(): Observable<BuyerInfo> {
    const buyerInfo: BuyerInfo = {
      id: 0, // temporary value
      userId: -1, // will be overwritten
      name: "",
      phoneNumber: "",
      pastOrdersIds: [],
      creditCards: [],
      shippingAddresses: [],
      cart: [],
      wishlist: [],
    };

    return this.addBuyerInfo(buyerInfo);
  }

  addBuyerInfo(buyerInfo: any): Observable<any> {
    console.log("adding buyer info")
    return this.http
      .post(this.buyerInformationURL, buyerInfo, this.httpOptions)
      .pipe(catchError(this.handleError<any>('addBuyerInfo')));
  }

  updateBuyerInfo(buyerInfo: any): Observable<BuyerInfo> {
    console.log("updating buyer info");
    return this.http
      .put<BuyerInfo>(this.buyerInformationURL, buyerInfo, this.httpOptions)
      .pipe(catchError(this.handleError<any>('updateBuyerInfo')));
  }

  deleteBuyerInfo(id: number): Observable<BuyerInfo> {
    const url = `${this.buyerInformationURL}/${id}`;
    return this.http
      .delete<BuyerInfo>(url, this.httpOptions)
      .pipe(catchError(this.handleError<any>('deleteBuyerInfo')));
  }

  getBuyerCart(): Observable<Cart> {
    const url = `${this.buyerInformationURL}/cart`;
    return this.http
      .get<Cart>(url)
      .pipe(catchError(this.handleError<any>(`getBuyerCart`)));
  }

  getBuyerInfoByUser(): Observable<BuyerInfo> {
    const url = `${this.buyerInformationURL}/this_user`;
    return this.http
        .get<BuyerInfo>(url, this.httpOptions)
        .pipe(catchError(this.handleError<any>('getBuyerInfoByUser')));  
  }
}
