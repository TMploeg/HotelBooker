import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordHelpDialogComponent } from './password-help-dialog.component';

describe('PasswordHelpDialogComponent', () => {
  let component: PasswordHelpDialogComponent;
  let fixture: ComponentFixture<PasswordHelpDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordHelpDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordHelpDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
