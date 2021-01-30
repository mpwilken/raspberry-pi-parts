import {browser, by, element, ElementFinder} from 'protractor';
import {environment} from '../../../src/environments/environment.e2e';

export class SigninPage {
  async login() {
    await browser.get('/signin');
    const usernameInput: ElementFinder = this.getUsernameInput();
    const passwordInput: ElementFinder = this.getPasswordInput();
    const signinButton: ElementFinder = this.getSigninButton();
    await usernameInput.sendKeys(environment.username);
    await passwordInput.sendKeys(environment.password);
    await signinButton.click();
  }

  getUsernameInput(): ElementFinder {
    return element(by.id('username'));
  }

  getPasswordInput() {
    return element(by.id('password'));
  }

  getSigninButton() {
    return element(by.tagName('app-login button'));
  }
}
