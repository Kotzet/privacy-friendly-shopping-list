package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.listadapter;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListDto;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.ShoppingListActivityCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.ProductsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.EditDialogFragment;

/**
 * Created by Chris on 05.06.2016.
 */
public class ListsItemViewHolder extends RecyclerView.ViewHolder
{
    private ListItemCache listItemCache;
    private ShoppingListActivityCache shoppingListCache;
    private ProductService productService;

    public ListsItemViewHolder(final View parent, ShoppingListActivityCache cache)
    {
        super(parent);
        this.listItemCache = new ListItemCache(parent);
        this.shoppingListCache = cache;
        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
    }

    public void processDto(ListDto dto)
    {
        listItemCache.getListNameTextView().setText(dto.getListName());

        int nrProducts = productService.getAllProducts(dto.getId()).size();
        listItemCache.getNrProductsTextView().setText(String.valueOf(nrProducts));
        setupPriorityIcon(dto);


        listItemCache.getListCard().setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(shoppingListCache.getActivity(), ProductsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MainActivity.LIST_NAME_KEY, dto.getListName());
                intent.putExtra(MainActivity.LIST_ID_KEY, dto.getId());
                shoppingListCache.getActivity().startActivity(intent);
            }
        });

        listItemCache.getListCard().setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                DialogFragment productFragement = EditDialogFragment.newInstance(dto, shoppingListCache);
                productFragement.show(shoppingListCache.getActivity().getSupportFragmentManager(), "Liste");

                return true;
            }
        });

    }

    public void setupPriorityIcon(ListDto dto)
    {
        // todo: do not use hard code values here
        if ( dto.getPriority().equals("0") )
        {
            listItemCache.getHighPriorityImageView().setVisibility(View.VISIBLE);
        }
        else
        {
            listItemCache.getHighPriorityImageView().setVisibility(View.GONE);
        }
    }


}