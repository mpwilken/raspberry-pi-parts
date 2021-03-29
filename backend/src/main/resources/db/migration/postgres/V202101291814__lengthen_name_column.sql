alter table PART alter column NAME VARCHAR(300) NOT NULL;
update PART set ORDER_DATE = PARSEDATETIME('31-december-17','dd-MMMM-yy') where ORDER_DATE IS NULL;
