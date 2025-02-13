import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBookFormComponent } from './new-book-form.component';

describe('NewBookFormComponent', () => {
  let component: NewBookFormComponent;
  let fixture: ComponentFixture<NewBookFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewBookFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewBookFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
