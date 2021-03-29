import {PartListPage} from './page-objects/part-list.po';
import {browser} from 'protractor';
import {HeaderSection} from './page-objects/parts.po';
import {SigninPage} from './page-objects/signin.po';

describe('View parts,', () => {
    let page: PartListPage;
    let partsList;
    let header: HeaderSection;

    beforeAll(async () => {
        browser.driver.manage().window().setSize(1000, 1000);
        await new SigninPage().login();
        await browser.waitForAngular();
        page = new PartListPage();
        await page.navigateTo(browser.params.prefix + '/parts/list');
        partsList = await page.getPartsList();
        header = new HeaderSection();
    });

    describe('when searching for parts,', () => {
        it('should find a part', async () => {
            await header.getHeader().getSearchInput().sendKeys('Relay');
            await header.getHeader().getSearchButton().click();
            page = await new PartListPage();
            partsList = await page.getPartsList();

            expect(await partsList.getRows().count()).toEqual(4);
            expect(await partsList.getRow(0).getName().getText()).toBe('3v Relay Board Power Switch');
            expect(await partsList.getRow(0).getCost().getText()).toBe('14.99');
            expect(await partsList.getRow(0).getQuantity().getText()).toBe('1');
            expect(await partsList.getRow(0).getDescription().getText()).toBe(
                '3v Relay Board Power Switch Relay Module 1 Channel Optocoupler');
        });
    });
});
