import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookModelCardComponent } from './book-model-card.component';

describe('BookModelCardComponent', () => {
  let component: BookModelCardComponent;
  let fixture: ComponentFixture<BookModelCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookModelCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookModelCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
