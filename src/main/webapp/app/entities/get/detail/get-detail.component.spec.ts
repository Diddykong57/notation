import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GetDetailComponent } from './get-detail.component';

describe('Component Tests', () => {
  describe('Get Management Detail Component', () => {
    let comp: GetDetailComponent;
    let fixture: ComponentFixture<GetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ get: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load get on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.get).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
