import {PartCreatePage} from './page-objects/part-create.po';
import {browser} from 'protractor';
import {HeaderSection} from './page-objects/parts.po';
import {SigninPage} from './page-objects/signin.po';

describe('Create part,', () => {
    let page: PartCreatePage;
    let header: HeaderSection;

    beforeAll(async () => {
        await new SigninPage().login();
        await browser.waitForAngular();
        page = new PartCreatePage();
        header = new HeaderSection();
    });

    describe('when adding a new part,', () => {
        let partCreateForm;
        beforeAll(async () => {
            browser.driver.manage().window().setSize(720, 700);
            await page.navigateTo(browser.params.prefix + '/parts/create');
            partCreateForm = await page.getPartCreateForm();
        });

        it('enters values for each and clicks submit', async () => {
            await partCreateForm.getNameInput().sendKeys('Name');
            await partCreateForm.getCostInput().sendKeys('Cost');
            await partCreateForm.getQuantityInput().sendKeys('Quantity');
            await partCreateForm.getDescriptionInput().sendKeys('Description');
            await partCreateForm.getUrlInput().sendKeys('Url');
            await partCreateForm.getOrderDateInput().clear();
            await partCreateForm.getOrderDateInput().sendKeys('2019-08-14');
            await partCreateForm.getOrderIdInput().sendKeys('OrderId');

            await partCreateForm.getSubmitButton().click();

            expect(await partCreateForm.getNameInput().getAttribute('value')).toBe('Name');
            expect(await partCreateForm.getCostInput().getAttribute('value')).toBe('Cost');
            expect(await partCreateForm.getQuantityInput().getAttribute('value')).toBe('Quantity');
            expect(await partCreateForm.getDescriptionInput().getAttribute('value')).toBe('Description');
            expect(await partCreateForm.getUrlInput().getAttribute('value')).toBe('Url');
            expect(await partCreateForm.getOrderDateInput().getAttribute('value')).toBe('2019-08-14');
            expect(await partCreateForm.getOrderIdInput().getAttribute('value')).toBe('OrderId');
        });
    });
});
