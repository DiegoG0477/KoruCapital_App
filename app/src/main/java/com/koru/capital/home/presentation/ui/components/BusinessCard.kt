package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.composables.icons.lucide.*
import com.koru.capital.R
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.home.presentation.viewmodel.BusinessCardUiModel
import com.koru.capital.core.ui.theme.*

@Composable
fun BusinessCard(
    business: BusinessCardUiModel,
    onSaveClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = KoruWhite)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(business.imageUrl ?: R.drawable.sample_business)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.sample_business),
                error = painterResource(R.drawable.sample_business),
                contentDescription = business.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {

                Text(
                    text = business.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = funnelSansFamily,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    business.category?.let { BusinessTag(icon = Lucide.Tag, text = it) }
                    business.location?.let { BusinessTag(icon = Lucide.MapPin, text = it) }
                    business.investmentRange?.let { BusinessTag(icon = Lucide.BadgeDollarSign, text = it) }
                    business.partnerCount?.let {
                        if (it > 0) {
                            BusinessTag(icon = Lucide.Users, text = "$it socios")
                        }
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 9.dp),
                    text = business.description,
                    fontFamily = funnelSansFamily,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = KoruDarkGray
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "Modelo de negocio:",
                    color = KoruOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = funnelSansFamily
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = business.businessModel,
                    fontFamily = funnelSansFamily,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Divider(modifier = Modifier.padding(vertical = 10.dp), color = KoruGrayBackground)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(business.ownerImageUrl ?: R.drawable.sample_profile)
                                .crossfade(true)
                                .transformations(coil3.transform.CircleCropTransformation())
                                .build(),
                            placeholder = painterResource(R.drawable.sample_profile),
                            error = painterResource(R.drawable.sample_profile),
                            contentDescription = "Owner Profile",
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        business.ownerName?.let {
                            Text(
                                text = it,
                                fontFamily = funnelSansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onLikeClick, modifier = Modifier.size(40.dp)) {
                            Icon(
                                if (business.isLiked) Lucide.Heart else Lucide.Heart,
                                contentDescription = "Like",
                                tint = if (business.isLiked) KoruRed else KoruDarkGray,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onSaveClick)
                                .padding(start = 4.dp)
                        ) {
                            IconButton(onClick = onSaveClick, modifier = Modifier.size(40.dp)) {
                                Icon(
                                    if (business.isSaved) Lucide.BookmarkCheck else Lucide.Bookmark,
                                    contentDescription = "Guardar",
                                    tint = if (business.isSaved) KoruOrange else KoruDarkGray,
                                    modifier = Modifier.size(21.dp)
                                )
                            }
                            business.savedCount?.let { count ->
                                if (count > 0) {
                                    Text(
                                        text = count.toString(),
                                        fontFamily = funnelSansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (business.isSaved) KoruOrange else KoruDarkGray,
                                        modifier = Modifier.padding(start = 0.dp, end = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
