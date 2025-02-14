import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookModelComponent } from './book-model.component';

describe('BookModelComponent', () => {
  let component: BookModelComponent;
  let fixture: ComponentFixture<BookModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookModelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
