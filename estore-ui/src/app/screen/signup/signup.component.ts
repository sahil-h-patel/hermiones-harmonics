import { Component } from '@angular/core';
import { AuthorizationService } from '../../service/authorization.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent {
  emailInputClass = '';
  passwordInputClass = '';

  constructor(private authorizationService: AuthorizationService) {}

  signup(email: string, password: string): void {
    console.log(email, password);

    this.authorizationService.signup(email, password);
  }
}
