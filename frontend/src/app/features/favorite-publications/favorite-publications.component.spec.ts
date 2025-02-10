import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoritePublicationsComponent } from './favorite-publications.component';

describe('FavoritePublicationsComponent', () => {
  let component: FavoritePublicationsComponent;
  let fixture: ComponentFixture<FavoritePublicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FavoritePublicationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoritePublicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
