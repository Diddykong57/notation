import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DegreeDetailComponent } from './degree-detail.component';

describe('Component Tests', () => {
  describe('Degree Management Detail Component', () => {
    let comp: DegreeDetailComponent;
    let fixture: ComponentFixture<DegreeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DegreeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ degree: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DegreeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DegreeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load degree on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.degree).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
